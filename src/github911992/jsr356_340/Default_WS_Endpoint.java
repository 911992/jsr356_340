
/*
jsr356_340
File: Default_WS_Endpoint.java
Created on: Jul 26, 2021 5:17:07 PM
    @author https://github.com/911992
 
History:
    initial version: 0.1(20210726)
 */
package github911992.jsr356_340;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author https://github.com/911992
 */
@ServerEndpoint(value = "/portal")
public class Default_WS_Endpoint {

    private static final Executor _ex = Executors.newFixedThreadPool(1,new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread _tr= new Thread(r);
            _tr.setDaemon(true);
            return _tr;
        }
    });

    private static volatile String LAST_SERVER_MESSAGE = "";

    public static String get_last_srv_msg() {
        return LAST_SERVER_MESSAGE;
    }

    static {
        System.out.println("Default_WS_Endpoint initialized");
        _ex.execute(new Runnable() {
            @Override
            public void run() {
                long _sleep;
                String _msg;
                while (0 == 0) {
                    _sleep = 4999 + (long) ((Math.random() * 9929.0));
                    try {
                        Thread.sleep(_sleep);
                    } catch (Throwable wtf) {
                    }
                    _msg = String.format("Server : timestamp here: %d , have a nice day.", System.currentTimeMillis());
                    Default_WS_Endpoint.message_all(_msg, null);
                }
            }
        });
    }

    private static final ArrayList<Session> sessions = new ArrayList<>(2);

    private static final String srv_message(String arg_msg) {
        String _msg = String.format("(%d):%s", System.currentTimeMillis(), arg_msg);
        return _msg;
    }

    public static final synchronized void message_all(String arg_msg, Session arg_exclude) {
        String _msg = srv_message(arg_msg);
        LAST_SERVER_MESSAGE = _msg;
        for (Session _ins : sessions) {
            if (arg_exclude != null && arg_exclude.equals(_ins)) {
                continue;
            }
            try {
                _ins.getAsyncRemote().sendText(_msg);
            } catch (Throwable wtf) {
            }
        }
    }

    public static final synchronized void add_session(Session arg_sess) {
        sessions.add(arg_sess);
    }

    public static final synchronized void remove_session(Session arg_sess) {
        sessions.remove(arg_sess);
    }

    @OnOpen
    public void myOnOpen(Session arg_session) {
        add_session(arg_session);
        message_all(String.format("Client id:%s chopped into the void", arg_session.getId()), null);
    }

    @OnMessage
    public void myOnMessage(String arg_msg,Session arg_sess) {
        message_all(String.format("Client %s: %s", arg_sess.getId(), arg_msg), null);
    }

    @OnClose
    public void myOnClose(CloseReason arg_reason,Session arg_sess) {
        remove_session(arg_sess);
        message_all(String.format("Client id:%s left the void (reason: %d:%s)", arg_sess.getId(), arg_reason.getCloseCode(), arg_reason.getReasonPhrase()), null);
    }

}
