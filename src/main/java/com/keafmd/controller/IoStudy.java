package com.keafmd.controller;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class IoStudy {
    public static void main(String[] args) throws IOException {
        File file =new File("/Users/dong");
        file.createNewFile();

    }
}
