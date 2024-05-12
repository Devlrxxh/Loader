package me.lrxh.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ByteClassLoader extends SecureClassLoader {

    private final byte[] jarBytes;
    private final Set<String> names;

    public ByteClassLoader(byte[] jarBytes) throws IOException {
        super(PluginLoader.class.getClassLoader());
        this.jarBytes = jarBytes;
        this.names = loadNames(jarBytes);
    }

    private Set<String> loadNames(byte[] jarBytes) throws IOException {
        Set<String> names = new LinkedHashSet<>(calculateInitialCapacity(jarBytes.length));
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(jarBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                names.add(entry.getName());
            }
        }
        return Collections.unmodifiableSet(names);
    }

    private int calculateInitialCapacity(int jarSize) {
        return jarSize / 100;
    }


    @Override
    public InputStream getResourceAsStream(String name) {
        if (!names.contains(name)) {
            return null;
        }

        try {
            ZipInputStream jis = new ZipInputStream(new ByteArrayInputStream(jarBytes));
            ZipEntry entry;
            while ((entry = jis.getNextEntry()) != null) {
                if (entry.getName().equals(name)) {
                    return jis;
                }
            }
            jis.close();
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }
        return null;
    }


    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            try (InputStream inputStream = getResourceAsStream(name.replace('.', '/') + ".class")) {
                if (inputStream != null) {
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    clazz = defineClass(name, bytes, 0, bytes.length);
                    if (resolve) {
                        resolveClass(clazz);
                    }
                } else {
                    clazz = super.loadClass(name, resolve);
                }
            } catch (IOException e) {
                clazz = super.loadClass(name, resolve);
            }
        }
        return clazz;
    }

}