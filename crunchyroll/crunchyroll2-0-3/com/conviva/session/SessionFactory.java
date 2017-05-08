// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.session;

import com.conviva.ConvivaContentInfo;
import java.util.Iterator;
import java.util.HashMap;
import com.conviva.utils.PlatformUtils;
import java.util.Map;

public class SessionFactory
{
    private int _nextSessionId;
    private Map<Integer, Session> _sessionsById;
    private PlatformUtils _utils;
    
    public SessionFactory() throws Exception {
        this._nextSessionId = 0;
        this._sessionsById = null;
        this._utils = null;
        this._utils = PlatformUtils.getInstance();
        this._nextSessionId = 0;
        this._sessionsById = new HashMap<Integer, Session>();
    }
    
    public void cleanup() {
        if (this._sessionsById != null) {
            final Iterator<Map.Entry<Integer, Session>> iterator = this._sessionsById.entrySet().iterator();
            while (iterator.hasNext()) {
                this.cleanupSession(iterator.next().getKey());
            }
        }
        this._sessionsById = null;
        this._nextSessionId = 0;
    }
    
    public void cleanupSession(final int n) {
        final Session session = this._sessionsById.get(n);
        if (session != null) {
            this._sessionsById.remove(n);
            this._utils.log("session id(" + n + ") is cleaned up and removed from sessionFactory");
            session.cleanup();
        }
    }
    
    public Session getSession(final int n) {
        return this._sessionsById.get(n);
    }
    
    public Session makeSession(final Object o, final ConvivaContentInfo convivaContentInfo, final Map<String, Object> map, final int n, final boolean b) throws Exception {
        final Session session = new Session(o, convivaContentInfo, map, b);
        this._sessionsById.put(n, session);
        this._utils.log("Session is created; session is about to start");
        session.start();
        return session;
    }
    
    public int newSessionId() {
        final int nextSessionId = this._nextSessionId;
        ++this._nextSessionId;
        return nextSessionId;
    }
}
