#include "berkelium-java.h"

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl__1setDelegate(JNIEnv* env, jobject self, jobject dlg)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->setDelegate(new JavaWindowDelegateProxy(env, dlg));
}

JNIEXPORT jint JNICALL Java_org_berkelium_java_impl_WindowImpl_getId(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return 0;
	return java->getWindow(self)->getId();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_setTransparent(JNIEnv* env, jobject self, jboolean val)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->setTransparent(val ? true : false);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_focus(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->focus();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_unfocus(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->unfocus();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_mouseMoved(JNIEnv* env, jobject self, jint x, jint y)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->mouseMoved(x, y);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_mouseButton(JNIEnv* env, jobject self, jint b, jboolean down)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->mouseButton(b, down ? true : false);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_mouseWheel(JNIEnv* env, jobject self, jint x, jint y)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->mouseWheel(x, y);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_textEvent(JNIEnv* env, jobject self, jstring str)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	jboolean iscopy;
	// FIXME: wchar_t / char
	const wchar_t* data = (const wchar_t*)env->GetStringUTFChars(str, &iscopy);
	jint len = env->GetStringUTFLength(str);
	java->getWindow(self)->textEvent(data, len);
	env->ReleaseStringUTFChars(str, (const char*)data);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_keyEvent(JNIEnv* env, jobject self, jboolean pressed, jint mods, jint vk_code, jint scancode)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->keyEvent(pressed ? true : false, mods, vk_code, scancode);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl__1resize(JNIEnv* env, jobject self, jint w, jint h)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->resize(w, h);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_adjustZoom(JNIEnv* env, jobject self, jint mode)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->adjustZoom(mode);
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_executeJavascript(JNIEnv* env, jobject self, jstring script)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->executeJavascript(java->jstring2WideString(script));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_insertCSS(JNIEnv* env, jobject self, jstring css, jstring elementId)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->insertCSS(java->jstring2WideString(css), java->jstring2WideString(elementId));
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_java_impl_WindowImpl_navigateTo(JNIEnv* env, jobject self, jstring url)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return false;
	jboolean iscopy;
	const char* data = env->GetStringUTFChars(url, &iscopy);
	jint len = env->GetStringUTFLength(url);
	jboolean ret = java->getWindow(self)->navigateTo(data, len);
	env->ReleaseStringUTFChars(url, data);
	return ret;
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_refresh(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->refresh();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_stop(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->stop();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_goBack(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->goBack();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_goForward(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->goForward();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_java_impl_WindowImpl_canGoBack(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return false;
	return java->getWindow(self)->canGoBack();
}

JNIEXPORT jboolean JNICALL Java_org_berkelium_java_impl_WindowImpl_canGoForward(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return false;
	return java->getWindow(self)->canGoForward();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_cut(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->cut();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_copy(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->copy();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_paste(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->paste();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_undo(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->undo();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_redo(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->redo();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_del(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->del();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_selectAll(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->selectAll();
}

/*
JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_filesSelected(JNIEnv* env, jobject self, jobject filesArray)
{
	Berkelium_Java_Env jEnv(env);
	//FIXME
	//java->getWindow(self)->filesSelected();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_synchronousScriptReturn(JNIEnv* env, jobject self, jobject handle, jobject result)
{
	Berkelium_Java_Env jEnv(env);
	//FIXME
	//java->getWindow(self)->synchronousBerkelium::ScriptReturn();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_bind(JNIEnv* env, jobject self, jstring lval, jobject rval)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::WideString str = jstring2WideString(lval);
	java->getWindow(self)->bind(str, Berkelium::Script::Variant::bindFunction(str, false));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_addBindOnStartLoading(JNIEnv* env, jobject self, jstring lval, jobject rval)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::WideString str = jstring2WideString(lval);
	java->getWindow(self)->addBindOnStartLoading(str, Berkelium::Script::Variant::bindFunction(str, false));
}
*/

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_addEvalOnStartLoading(JNIEnv* env, jobject self, jstring script)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->addEvalOnStartLoading(java->jstring2WideString(script));
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl_clearStartLoading(JNIEnv* env, jobject self)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->getWindow(self)->clearStartLoading();
}

JNIEXPORT jlong JNICALL Java_org_berkelium_java_impl_WindowImpl__1init(JNIEnv* env, jobject self, jlong ctx)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return 0;
	jlong handle = (jlong)Berkelium::Window::create((Berkelium::Context*)ctx);
	java->add(handle, self);
	return handle;
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_WindowImpl__1destroy(JNIEnv* env, jobject self, jlong handle)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	java->remove(handle);
	((Berkelium::Window*)handle)->destroy();
}
