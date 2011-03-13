#define BERKELIUM_JAVA_ATTACH_THREAD_TO_JVM 0
extern JNIEnv* Berkelium_Java_Update_JNIEnv;
class JavaWindowDelegateProxy : public Berkelium::WindowDelegate {
private:
#if BERKELIUM_JAVA_ATTACH_THREAD_TO_JVM
	JavaVM* jvm;
#endif
	jobject globalDelegate;
	
public:
	JavaWindowDelegateProxy(JNIEnv* env, jobject localDelegate) {
		globalDelegate = env->NewGlobalRef(localDelegate);
#if BERKELIUM_JAVA_ATTACH_THREAD_TO_JVM
		env->GetJavaVM(&jvm);
		std::cout << "JavaWindowDelegateProxy(" << (int)jvm << ", " << (int)globalDelegate << std::endl;
#endif
	}
	
	virtual void onPaint(
		Berkelium::Window *wini,
		const unsigned char *bitmap_in,
		const Berkelium::Rect &bitmap_rect,
		size_t num_copy_rects,
		const Berkelium::Rect *copy_rects,
		int dx,
		int dy,
		const Berkelium::Rect &scroll_rect)
	{
		const char* sig = "(Lorg/berkelium/Window;Lorg/berkelium/Buffer;Lorg/berkelium/Rect;[Lorg/berkelium/Rect;IILorg/berkelium/Rect;)V";
		JNIEnv* env = attachCurrentThread();
		jobject dlg = getLocalDelegate(env);
		jmethodID jmid = getMethod(env, dlg, "onPaint", sig);
		if (jmid == 0)return;
		jobject win = Berkelium_Java_Registry_get(env, (jlong)wini);
		if (win == 0)return;
		jobject buffer = Berkelium_Java_Buffer(env, bitmap_in, 4 * bitmap_rect.width() * bitmap_rect.height());
		if (buffer == 0)return;
		jobject inRect = Berkelium_Java_Rect(env, bitmap_rect);
		jobject copyRects = Berkelium_Java_Rects(env, num_copy_rects, copy_rects);
		jobject scrollRect = Berkelium_Java_Rect(env, scroll_rect);
		env->CallVoidMethod(dlg, jmid, win, buffer, inRect, copyRects, dx, dy, scrollRect);
		detachCurrentThread();
	}

	virtual void onAddressBarChanged(Berkelium::Window *win, Berkelium::URLString newURL) {
		std::cout << (void*)win << "*** onAddressBarChanged " << newURL << std::endl;
	}
	virtual void onStartLoading(Berkelium::Window *win, Berkelium::URLString newURL) {
		std::cout << (void*)win << "*** onStartLoading " << newURL << std::endl;
	}
	
	virtual void onLoad(Berkelium::Window *win) {
		callWindowFunc("onLoad", win);
	}
	
	virtual void onCrashedWorker(Berkelium::Window *win) {
		callWindowFunc("onCrashedWorker", win);
	}
	virtual void onCrashedPlugin(Berkelium::Window *win, Berkelium::WideString pluginName) {
		std::wcout << L"*** onCrashedPlugin " << pluginName << std::endl;
	}
	virtual void onProvisionalLoadError(Berkelium::Window *win, Berkelium::URLString url,
										int errorCode, bool isMainFrame) {
		std::cout << "*** onProvisionalLoadError " << url << ": " << errorCode;
		if (isMainFrame) std::cout << " (main frame)";
		std::cout << std::endl;
	}
	virtual void onConsoleMessage(Berkelium::Window *win, Berkelium::WideString message,
								  Berkelium::WideString sourceId, int line_no) {
		std::wcout << L"*** onConsoleMessage " << message << L" from "
				   << sourceId << L" line " << line_no << std::endl;
	}
	virtual void onScriptAlert(Berkelium::Window *win, Berkelium::WideString message,
							  Berkelium::WideString defaultValue, Berkelium::URLString url,
							  int flags, bool &success, Berkelium::WideString &value) {
		std::wcout << L"*** onBerkelium::ScriptAlert " << message << std::endl;
	}
	virtual void onNavigationRequested(Berkelium::Window *win, Berkelium::URLString newURL,
									   Berkelium::URLString referrer, bool isNewWindow,
									   bool &cancelDefaultAction) {
		std::cout << (void*)win << "*** onNavigationRequested " << newURL << " by " << referrer
				  << (isNewWindow?"  (new window)" : " (same window)") << std::endl;
	}
	virtual void onLoadingStateChanged(Berkelium::Window *win, bool isLoading) {
		std::cout << (void*)win << "*** onLoadingStateChanged "
				  << (isLoading?"started":"stopped") << std::endl;
	}
	virtual void onTitleChanged(Berkelium::Window *win, Berkelium::WideString title) {
		std::wcout << L"*** onTitleChanged " << title << std::endl;
	}
	virtual void onTooltipChanged(Berkelium::Window *win, Berkelium::WideString text) {
		std::wcout << L"*** onTooltipChanged " << text << std::endl;
	}
	virtual void onCrashed(Berkelium::Window *win) {
		callWindowFunc("onCrashed", win);
	}
	virtual void onUnresponsive(Berkelium::Window *win) {
		callWindowFunc("onUnresponsive", win);
	}
	virtual void onResponsive(Berkelium::Window *win) {
		callWindowFunc("onResponsive", win);
	}
	virtual void onCreatedWindow(Berkelium::Window *win, Berkelium::Window *newWindow,
								 const Berkelium::Rect &initialRect) {
		std::cout << (void*)win << "*** onCreatedWindow " << (void*)newWindow<<" "
				  << initialRect.mLeft << "," << initialRect.mTop << ": "
				  << initialRect.mWidth << "x" << initialRect.mHeight << std::endl;
		if (initialRect.mWidth < 1 || initialRect.mHeight < 1) {
			//newWindow->resize(this->width, this->height);
		}
		newWindow->setDelegate(this);
	}
	virtual void onWidgetCreated(Berkelium::Window *win, Berkelium::Widget *newWidget, int zIndex) {
		std::cout << "*** onWidgetCreated " << newWidget << " index " << zIndex << std::endl;
	}
	virtual void onWidgetResize(Berkelium::Window *win, Berkelium::Widget *wid, int newWidth, int newHeight) {
		std::cout << "*** onWidgetResize " << wid << " "
				  << newWidth << "x" << newHeight << std::endl;
	}
	virtual void onWidgetMove(Berkelium::Window *win, Berkelium::Widget *wid, int newX, int newY) {
		std::cout << "*** onWidgetMove " << wid << " "
				  << newX << "," << newY << std::endl;
	}
	virtual void onShowContextMenu(Berkelium::Window *win,
								   const Berkelium::ContextMenuEventArgs& args) {
		std::cout << "*** onShowContextMenu at " << args.mouseX << "," << args.mouseY;
		std::cout << std::endl;
	}
	
	virtual void onJavaScriptCallback(Berkelium::Window *win, void* replyMsg, Berkelium::URLString url, Berkelium::WideString funcName, Berkelium::Script::Variant *args, size_t numArgs) {
		std::cout << "*** onJavaBerkelium::ScriptCallback at URL " << url << ", "
				  << (replyMsg?"synchronous":"async") << std::endl;
		std::wcout << L"	Function name: " << funcName << std::endl;
		for (size_t i = 0; i < numArgs; i++) {
			Berkelium::WideString jsonStr = toJSON(args[i]);
			std::wcout << L"	Argument " << i << ": ";
			if (args[i].type() == Berkelium::Script::Variant::JSSTRING) {
				std::wcout << L"(string) " << args[i].toString() << std::endl;
			} else {
				std::wcout << jsonStr << std::endl;
			}
			Berkelium::Script::toJSON_free(jsonStr);
		}
		if (replyMsg) {
			win->synchronousScriptReturn(replyMsg, numArgs ? args[0] : Berkelium::Script::Variant());
		}
	}
	
	/** Display a file chooser dialog, if necessary. The value to be returned should go ______.
	 * \param win  Window instance that fired this event.
	 * \param mode  Type of file chooser expected. See FileChooserType.
	 * \param title  Title for dialog. "Open" or "Save" should be used if empty.
	 * \param defaultFile  Default file to select in dialog.
	 */
	virtual void onRunFileChooser(Berkelium::Window *win, int mode, Berkelium::WideString title, Berkelium::FileString defaultFile) {
		std::wcout << L"*** onRunFileChooser type " << mode << L", title " << title << L":" << std::endl;
#ifdef _WIN32
		std::wcout <<
#else
		std::cout <<
#endif
			defaultFile << std::endl;
	
		win->filesSelected(NULL);
	}
	
	virtual void onExternalHost(
		Berkelium::Window *win,
		Berkelium::WideString message,
		Berkelium::URLString origin,
		Berkelium::URLString target)
	{
		std::cout << "*** onExternalHost at URL from "<<origin<<" to "<<target<<":"<<std::endl;
		std::wcout << message<<std::endl;
	}
	
private:
	JNIEnv* attachCurrentThread() {
#if BERKELIUM_JAVA_ATTACH_THREAD_TO_JVM
		JNIEnv* env = 0;
#ifdef JNI_VERSION_1_2
		jint res = jvm->AttachCurrentThread((void**)&env, NULL);
#else
		jint res = jvm->AttachCurrentThread(&env, NULL);
#endif
		return env;
#else
		return Berkelium_Java_Update_JNIEnv;
#endif
	}
	void detachCurrentThread() {
#if BERKELIUM_JAVA_ATTACH_THREAD_TO_JVM
		jvm->DetachCurrentThread();
#endif
	}
	
	jobject getLocalDelegate(JNIEnv* env) {
		return env->NewGlobalRef(globalDelegate);
	}

	void callWindowFunc(const char* func, Berkelium::Window *wini) {
		const char* sig = "(Lorg/berkelium/Window;)V";
		JNIEnv* env = attachCurrentThread();
		jobject obj = getLocalDelegate(env);
		jmethodID jmid = getMethod(env, obj, func, sig);
		jobject win = Berkelium_Java_Registry_get(env, (jint)wini);
		env->CallVoidMethod(obj, jmid, win);
		detachCurrentThread();
	}
	
	jmethodID getMethod(JNIEnv* env, jobject obj, const char* func, const char* sig) {
		jclass cls = env->GetObjectClass(obj);
		return env->GetMethodID(cls, func, sig);
	}
};
