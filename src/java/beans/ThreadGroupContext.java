/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.beans;

import org.ifcx.backportbeans.finder.BeanInfoFinder;
import org.ifcx.backportbeans.finder.PropertyEditorFinder;

import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The {@code ThreadGroupContext} is an application-dependent
 * context referenced by the specific {@link ThreadGroup}.
 * This is a replacement for the {@link sun.awt.AppContext}.
 *
 * @author  Sergey Malenkov
 */
final class ThreadGroupContext {

    private static final Map<ThreadGroup, ThreadGroupContext> contexts = new WeakHashMap<ThreadGroup, ThreadGroupContext>();

    /**
     * Returns the appropriate {@code AppContext} for the caller,
     * as determined by its {@code ThreadGroup}.
     *
     * @return  the application-dependent context
     */
    static ThreadGroupContext getContext() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        synchronized (contexts) {
            ThreadGroupContext context = contexts.get(group);
            if (context == null) {
                context = new ThreadGroupContext();
                contexts.put(group, context);
            }
            return context;
        }
    }

    private volatile boolean isDesignTime;
    private volatile Boolean isGuiAvailable;

    private Map<Class<?>, BeanInfo> beanInfoCache;
    private BeanInfoFinder beanInfoFinder;
    private PropertyEditorFinder propertyEditorFinder;


    boolean isDesignTime() {
        return this.isDesignTime;
    }

    void setDesignTime(boolean isDesignTime) {
        this.isDesignTime = isDesignTime;
    }


    boolean isGuiAvailable() {
        Boolean isGuiAvailable = this.isGuiAvailable;
        return (isGuiAvailable != null)
                ? isGuiAvailable.booleanValue()
                : !GraphicsEnvironment.isHeadless();
    }

    void setGuiAvailable(boolean isGuiAvailable) {
        this.isGuiAvailable = Boolean.valueOf(isGuiAvailable);
    }


    BeanInfo getBeanInfo(Class<?> type) {
        return (this.beanInfoCache != null)
                ? this.beanInfoCache.get(type)
                : null;
    }

    BeanInfo putBeanInfo(Class<?> type, BeanInfo info) {
        if (this.beanInfoCache == null) {
            this.beanInfoCache = new WeakHashMap<Class<?>, BeanInfo>();
        }
        return this.beanInfoCache.put(type, info);
    }

    void removeBeanInfo(Class<?> type) {
        if (this.beanInfoCache != null) {
            this.beanInfoCache.remove(type);
        }
    }

    void clearBeanInfoCache() {
        if (this.beanInfoCache != null) {
            this.beanInfoCache.clear();
        }
    }


    synchronized BeanInfoFinder getBeanInfoFinder() {
        if (this.beanInfoFinder == null) {
            this.beanInfoFinder = new BeanInfoFinder();
        }
        return this.beanInfoFinder;
    }

    synchronized PropertyEditorFinder getPropertyEditorFinder() {
        if (this.propertyEditorFinder == null) {
            this.propertyEditorFinder = new PropertyEditorFinder();
        }
        return this.propertyEditorFinder;
    }
}
