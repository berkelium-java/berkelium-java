#include <org_berkelium_java_impl_WindowImpl.h>

JNIEXPORT jobject JNICALL Java_org_berkelium_java_impl_WindowImpl_getWidget(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->getWidget();
	return self;
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_setDelegate(JNIEnv* env, jobject self, jobject dlg)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->setDelegate(new JavaWindowDelegateProxy(dlg));
}

JNIEXPORT jobject JNICALL Java_org_berkelium_java_impl_WindowImpl_getWidgetAtPoint(JNIEnv* env, jobject self, jint x, jint y, jboolean returnRootIfOutside)
{
	Berkelium_Java_Env jEnv(env);
	//FIXME
	getWindow(self)->getWidgetAtPoint(x, y, returnRootIfOutside ? true : false);
	return self;
}

JNIEXPORT jint JNICALL Java_org_berkelium_java_impl_WindowImpl_getId(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	return getWindow(self)->getId();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_setTransparent(JNIEnv* env, jobject self, jboolean val)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->setTransparent(val ? true : false);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_focus(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->focus();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_unfocus(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->unfocus();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_mouseMoved(JNIEnv* env, jobject self, jint x, jint y)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->mouseMoved(x, y);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_mouseButton(JNIEnv* env, jobject self, jint b, jboolean down)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->mouseButton(b, down ? true : false);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_mouseWheel(JNIEnv* env, jobject self, jint x, jint y)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->mouseWheel(x, y);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_textEvent(JNIEnv* env, jobject self, jstring str)
{
	Berkelium_Java_Env jEnv(env);
	jboolean iscopy;
	// FIXME: wchar_t / char
	const wchar_t* data = (const wchar_t*)env->GetStringUTFChars(str, &iscopy);
	jint len = env->GetStringUTFLength(str);
	getWindow(self)->textEvent(data, len);
	env->ReleaseStringUTFChars(str, (const char*)data);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_keyEvent(JNIEnv* env, jobject self, jboolean pressed, jint mods, jint vk_code, jint scancode)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->keyEvent(pressed ? true : false, mods, vk_code, scancode);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_resize(JNIEnv* env, jobject self, jint w, jint h)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->resize(w, h);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_adjustZoom(JNIEnv* env, jobject self, jint mode)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->adjustZoom(mode);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_executeJavascript(JNIEnv* env, jobject self, jstring script)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->executeJavascript(jstring2WideString(script));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_insertCSS(JNIEnv* env, jobject self, jstring css, jstring elementId)
{
	Berkelium_Java_Env jEnv(env);
	//FIXME
	//getWindow(self)->insertCSS();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_java_impl_WindowImpl_navigateTo(JNIEnv* env, jobject self, jstring url)
{
	Berkelium_Java_Env jEnv(env);
	jboolean iscopy;
	const char* data = env->GetStringUTFChars(url, &iscopy);
	jint len = env->GetStringUTFLength(url);
	jboolean ret = getWindow(self)->navigateTo(data, len);
	//fprintf(stderr, "navigateTo(%d):'%s'\n", len, data);
	env->ReleaseStringUTFChars(url, data);
	return ret;
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_refresh(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->refresh();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_stop(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->stop();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_goBack(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->goBack();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_goForward(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->goForward();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_java_impl_WindowImpl_canGoBack(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	return getWindow(self)->canGoBack();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_java_impl_WindowImpl_canGoForward(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	return getWindow(self)->canGoForward();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_cut(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->cut();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_copy(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->copy();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_paste(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->paste();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_undo(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->undo();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_redo(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->redo();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_del(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->del();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_selectAll(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->selectAll();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_filesSelected(JNIEnv* env, jobject self, jobject filesArray)
{
	Berkelium_Java_Env jEnv(env);
	//FIXME
	//getWindow(self)->filesSelected();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_synchronousScriptReturn(JNIEnv* env, jobject self, jobject handle, jobject result)
{
	Berkelium_Java_Env jEnv(env);
	//FIXME
	//getWindow(self)->synchronousBerkelium::ScriptReturn();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_bind(JNIEnv* env, jobject self, jstring lval, jobject rval)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::WideString str = jstring2WideString(lval);
	getWindow(self)->bind(str, Berkelium::Script::Variant::bindFunction(str, false));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_addBindOnStartLoading(JNIEnv* env, jobject self, jstring lval, jobject rval)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::WideString str = jstring2WideString(lval);
	getWindow(self)->addBindOnStartLoading(str, Berkelium::Script::Variant::bindFunction(str, false));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_addEvalOnStartLoading(JNIEnv* env, jobject self, jstring script)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->addEvalOnStartLoading(jstring2WideString(script));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_clearStartLoading(JNIEnv* env, jobject self)
{
	Berkelium_Java_Env jEnv(env);
	getWindow(self)->clearStartLoading();
}

JNIEXPORT jlong JNICALL Java_org_berkelium_java_impl_WindowImpl__1init(JNIEnv* env, jobject self, jlong ctx)
{
	Berkelium_Java_Env jEnv(env);
	jlong handle = (jlong)Berkelium::Window::create((Berkelium::Context*)ctx);
	Berkelium_Java_Registry_add(handle, self);
	return handle;
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl__1destroy(JNIEnv* env, jobject self, jlong handle)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium_Java_Registry_remove(handle);
	((Berkelium::Window*)handle)->destroy();
}
