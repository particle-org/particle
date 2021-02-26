package com.particle.game.server.plugin.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
