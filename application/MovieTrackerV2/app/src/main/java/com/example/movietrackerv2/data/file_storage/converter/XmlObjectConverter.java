package com.example.movietrackerv2.data.file_storage.converter;

import android.util.Log;

import com.example.movietrackerv2.data.file_storage.Permissions;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

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

// the purpose of this class is to provide methods for converting a string in xml-format to an object using xstream
public class XmlObjectConverter<T> implements ObjectConverter {

    private XStream xStream;

    public XmlObjectConverter(XStream xStream) {
        Permissions.setPermissions(xStream);
        this.xStream = xStream;
    }

    // takes a string representing an xml file's contents and parses an object from it. Upon success returns this object, else returns null.
    @Override
    public Object convertToObject(String content) {
        T object = null;
        if (!content.isEmpty()) {
            try {
                object = (T) xStream.fromXML(content);
            } catch (CannotResolveClassException e) {
                Log.e(null, "Cannot resolve class at convertToObject, XmlObjectConverter");
            }
        }
        return object;
    }
}
