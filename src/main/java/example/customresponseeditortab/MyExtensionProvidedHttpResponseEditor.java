/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customresponseeditortab;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static burp.api.montoya.core.ByteArray.byteArray;

class MyExtensionProvidedHttpResponseEditor implements ExtensionProvidedHttpResponseEditor
{
    private final RawEditor responseEditor;
    private HttpRequestResponse requestResponse;
    private final MontoyaApi api;


    MyExtensionProvidedHttpResponseEditor(MontoyaApi api, EditorCreationContext creationContext)
    {
        this.api = api;

        if (creationContext.editorMode() == EditorMode.READ_ONLY)
        {
            responseEditor = api.userInterface().createRawEditor(EditorOptions.READ_ONLY);
        }
        else {
            responseEditor = api.userInterface().createRawEditor();
        }
    }

    @Override
    public HttpResponse getResponse()
    {
        HttpResponse response;

        if (responseEditor.isModified())
        {
            response = requestResponse.response();
        }
        else
        {
            response = requestResponse.response();
        }

        return response;
    }

    @Override
    public void setRequestResponse(HttpRequestResponse requestResponse)
    {
        this.requestResponse = requestResponse;
        ByteArray output;

        try
        {
            output = byteArray(convertUnicodeToCh(requestResponse.response().bodyToString()).getBytes("UTF-8"));
        }
        catch (Exception e)
        {
            output = byteArray("No unicode found");
        }
        this.responseEditor.setContents(output);
    }

    private static String convertUnicodeToCh(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\w{4}))");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            char singleChar = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), singleChar + "");
        }
        return str;
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse requestResponse)
    {
        return requestResponse.response().bodyToString().contains("\\u");
        //return true;
    }

    @Override
    public String caption()
    {
        return "showUnicode";
    }

    @Override
    public Component uiComponent()
    {
        return responseEditor.uiComponent();
    }

    @Override
    public Selection selectedData()
    {
        return responseEditor.selection().isPresent() ? responseEditor.selection().get() : null;
    }

    @Override
    public boolean isModified()
    {
        return responseEditor.isModified();
    }
}