package com.example.movietrackerv2.data.file_storage.writer;

import com.example.movietrackerv2.data.file_storage.Permissions;
import com.example.movietrackerv2.data.file_storage.locator.FileLocator;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/* Xstream license:

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of
conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of
conditions and the following disclaimer in the documentation and/or other materials provided
with the distribution.

3. Neither the name of XStream nor the names of its contributors may be used to endorse
or promote products derived from this software without specific prior written
permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
DAMAGE.

https://x-stream.github.io/license.html

 */

// the purpose of this class is to provide methos for writing an object in xml format to file using XStream
public class XmlObjectWriter<T> implements ObjectWriter {

    private XStream xstream;

    public XmlObjectWriter(XStream xstream) {
        Permissions.setPermissions(xstream);
        this.xstream = xstream;
    }

    // writes String content to a file.
    private void writeFile(String content, File file) {

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fosw = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fosw);
            osw.write(content);
            osw.close();
        } catch (IOException exception) {
            System.out.println("error at writeFile\n");
            return;
        }

        System.out.println("Object written successfully to file: " + file.getAbsolutePath());
    }

    // uses XStream to turn the given object to xml format string. The result will be saved to a file determined by the given FileLocator by calling writeFile method for the content.
    @Override
    public void writeToFile(Object object, FileLocator fileLocator) {
        File file = fileLocator.locateFile();
        String xmlContent = xstream.toXML(object);
        writeFile(xmlContent, file);
    }
}
