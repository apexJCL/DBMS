package io.github.apexjcl.utils;

/**
 * Created by José Carlos López on 14/10/2016.
 */
public class Utils {

    /**
     * Concatenates a path, name and extension, returns full path
     *
     * TODO: optimize to use Stringbuilder
     * @param path
     * @param name
     * @param extension
     * @return
     */
    public static String concatFilepath(String path, String name, String extension){
        return path + name + "." + extension;
    }
}
