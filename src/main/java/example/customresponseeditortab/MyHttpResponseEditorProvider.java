/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customresponseeditortab;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import burp.api.montoya.ui.editor.extension.HttpResponseEditorProvider;

class MyHttpResponseEditorProvider implements HttpResponseEditorProvider
{
    private final MontoyaApi api;

    MyHttpResponseEditorProvider(MontoyaApi api)
    {
        this.api = api;
    }

    @Override
    public ExtensionProvidedHttpResponseEditor provideHttpResponseEditor(EditorCreationContext creationContext)
    {
        return new MyExtensionProvidedHttpResponseEditor(api, creationContext);
    }
}
