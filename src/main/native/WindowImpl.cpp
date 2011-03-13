#include <org_berkelium_WindowImpl.h>

JNIEXPORT jobject JNICALL Java_org_berkelium_WindowImpl_getWidget(JNIEnv* env, jobject self)
{
	//FIXME
	getWindow(env, self)->getWidget();
	return self;
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_setDelegate(JNIEnv* env, jobject self, jobject dlg)
{
	getWindow(env, self)->setDelegate(new JavaWindowDelegateProxy(env, dlg));
}

JNIEXPORT jobject JNICALL Java_org_berkelium_WindowImpl_getWidgetAtPoint(JNIEnv* env, jobject self, jint x, jint y, jboolean returnRootIfOutside)
{
	//FIXME
	getWindow(env, self)->getWidgetAtPoint(x, y, returnRootIfOutside ? true : false);
	return self;
}

JNIEXPORT jint JNICALL Java_org_berkelium_WindowImpl_getId(JNIEnv* env, jobject self)
{
	return getWindow(env, self)->getId();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_setTransparent(JNIEnv* env, jobject self, jboolean val)
{
	getWindow(env, self)->setTransparent(val ? true : false);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_focus(JNIEnv* env, jobject self)
{
	getWindow(env, self)->focus();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_unfocus(JNIEnv* env, jobject self)
{
	getWindow(env, self)->unfocus();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_mouseMoved(JNIEnv* env, jobject self, jint x, jint y)
{
	getWindow(env, self)->mouseMoved(x, y);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_mouseButton(JNIEnv* env, jobject self, jint b, jboolean down)
{
	getWindow(env, self)->mouseButton(b, down ? true : false);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_mouseWheel(JNIEnv* env, jobject self, jint x, jint y)
{
	getWindow(env, self)->mouseWheel(x, y);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_textEvent(JNIEnv* env, jobject self, jstring str)
{
	jboolean iscopy;
	// FIXME: wchar_t / char
	const wchar_t* data = (const wchar_t*)env->GetStringUTFChars(str, &iscopy);
	jint len = env->GetStringUTFLength(str);
	getWindow(env, self)->textEvent(data, len);
	env->ReleaseStringUTFChars(str, (const char*)data);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_keyEvent(JNIEnv* env, jobject self, jboolean pressed, jint mods, jint vk_code, jint scancode)
{
	getWindow(env, self)->keyEvent(pressed ? true : false, mods, vk_code, scancode);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_resize(JNIEnv* env, jobject self, jint w, jint h)
{
	getWindow(env, self)->resize(w, h);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_adjustZoom(JNIEnv* env, jobject self, jint mode)
{
	getWindow(env, self)->adjustZoom(mode);
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_executeJavaScript(JNIEnv* env, jobject self, jstring script)
{
	/*
	jboolean iscopy;
	// FIXME: wchar_t / char
	const wchar_t* data = (const wchar_t*)env->GetStringUTFChars(str, &iscopy);
	jint len = env->GetStringUTFLength(str);
	getWindow(env, self)->executeJavaBerkelium::Script(data, len);
	env->ReleaseStringUTFChars(str, (const char*)data);
	*/
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_insertCSS(JNIEnv* env, jobject self, jstring css, jstring elementId)
{
	//FIXME
	//getWindow(env, self)->insertCSS();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_WindowImpl_navigateTo(JNIEnv* env, jobject self, jstring url)
{
	jboolean iscopy;
	const char* data = env->GetStringUTFChars(url, &iscopy);
	jint len = env->GetStringUTFLength(url);
	jboolean ret = getWindow(env, self)->navigateTo(data, len);
	//fprintf(stderr, "navigateTo(%d):'%s'\n", len, data);
	env->ReleaseStringUTFChars(url, data);
	return ret;
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_refresh(JNIEnv* env, jobject self)
{
	getWindow(env, self)->refresh();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_stop(JNIEnv* env, jobject self)
{
	getWindow(env, self)->stop();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_goBack(JNIEnv* env, jobject self)
{
	getWindow(env, self)->goBack();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_goForward(JNIEnv* env, jobject self)
{
	getWindow(env, self)->goForward();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_WindowImpl_canGoBack(JNIEnv* env, jobject self)
{
	return getWindow(env, self)->canGoBack();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_WindowImpl_canGoForward(JNIEnv* env, jobject self)
{
	return getWindow(env, self)->canGoForward();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_cut(JNIEnv* env, jobject self)
{
	getWindow(env, self)->cut();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_copy(JNIEnv* env, jobject self)
{
	getWindow(env, self)->copy();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_paste(JNIEnv* env, jobject self)
{
	getWindow(env, self)->paste();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_undo(JNIEnv* env, jobject self)
{
	getWindow(env, self)->undo();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_redo(JNIEnv* env, jobject self)
{
	getWindow(env, self)->redo();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_del(JNIEnv* env, jobject self)
{
	getWindow(env, self)->del();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_selectAll(JNIEnv* env, jobject self)
{
	getWindow(env, self)->selectAll();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_filesSelected(JNIEnv* env, jobject self, jobject filesArray)
{
	//FIXME
	//getWindow(env, self)->filesSelected();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_synchronousScriptReturn(JNIEnv* env, jobject self, jobject handle, jobject result)
{
	//FIXME
	//getWindow(env, self)->synchronousBerkelium::ScriptReturn();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_bind(JNIEnv* env, jobject self, jstring lval, jobject rval)
{
	//FIXME
	//getWindow(env, self)->bind();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_addBindOnStartLoading(JNIEnv* env, jobject self, jstring lval, jobject rval)
{
	//FIXME
	//getWindow(env, self)->addBindOnStartLoading();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_addEvalOnStartLoading(JNIEnv* env, jobject self, jstring script)
{
	//FIXME
	//getWindow(env, self)->addEvalOnStartLoading();
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl_clearStartLoading(JNIEnv* env, jobject self)
{
	getWindow(env, self)->clearStartLoading();
}

JNIEXPORT jlong JNICALL Java_org_berkelium_WindowImpl__1init(JNIEnv* env, jobject self, jlong ctx)
{
	jlong handle = (jlong)Berkelium::Window::create((Berkelium::Context*)ctx);
	Berkelium_Java_Registry_add(env, handle, self);
	return handle;
}

JNIEXPORT void JNICALL Java_org_berkelium_WindowImpl__1destroy(JNIEnv* env, jobject self, jlong handle)
{
	Berkelium_Java_Registry_remove(env, handle);
	((Berkelium::Window*)handle)->destroy();
}
