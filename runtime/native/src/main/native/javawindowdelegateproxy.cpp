#if WIN32
#include <windows.h>
#ifndef CP_UTF8
#define CP_UTF8 65001
#endif
#endif

class JavaWindowDelegateProxy : public Berkelium::WindowDelegate {
private:
	jobject globalDelegate;
	
public:
	JavaWindowDelegateProxy(jobject localDelegate) {
		globalDelegate = Berkelium_Java_Env::get()->NewGlobalRef(localDelegate);
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
		callFuncA(
			"onLoad",
			"(Lorg/berkelium/java/Window;Lorg/berkelium/java/Buffer;Lorg/berkelium/java/Rect;[Lorg/berkelium/java/Rect;IILorg/berkelium/java/Rect;)V",
			map(wini),
			map(bitmap_in, 4 * bitmap_rect.width() * bitmap_rect.height()),
			map(bitmap_rect),
			map(num_copy_rects, copy_rects),
			dx,
			dy,
			map(scroll_rect)
		);
	}

	virtual void onAddressBarChanged(Berkelium::Window *win, Berkelium::URLString newURL) {
		callFunc("onAddressBarChanged", win, map(newURL));
	}
	virtual void onStartLoading(Berkelium::Window *win, Berkelium::URLString newURL) {
		callFunc("onStartLoading", win, map(newURL));
	}
	
	virtual void onLoad(Berkelium::Window *win) {
		callFunc("onLoad", win);
	}
	
	virtual void onCrashedWorker(Berkelium::Window *win) {
		callFunc("onCrashedWorker", win);
	}

	virtual void onCrashedPlugin(Berkelium::Window *win, Berkelium::WideString pluginName) {
		callFunc("onCrashedPlugin", win, map(pluginName));
	}

	virtual void onProvisionalLoadError(Berkelium::Window *win, Berkelium::URLString url,
										int errorCode, bool isMainFrame) {
		callFunc("onProvisionalLoadError", win, map(url), errorCode, isMainFrame);
	}

	virtual void onConsoleMessage(Berkelium::Window *win, Berkelium::WideString message,
								  Berkelium::WideString sourceId, int line_no) {
		callFunc("onConsoleMessage", win, map(message), map(sourceId), line_no);
	}

	virtual void onScriptAlert(Berkelium::Window *win, Berkelium::WideString message,
							  Berkelium::WideString defaultValue, Berkelium::URLString url,
							  int flags, bool &success, Berkelium::WideString &value) {
		callFuncA(
			"onScriptAlert",
			"(Lorg/berkelium/java/Window;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I[Z[Ljava/lang/String;)V",
			map(win),
			map(message),
			map(defaultValue),
			map(url),
			flags,
			mapRw(success),
			mapRw(value)
		);
		// FIXME: unmap(success)
		// FIXME: unmap(value)
	}

	virtual void onNavigationRequested(Berkelium::Window *win, Berkelium::URLString newURL,
									   Berkelium::URLString referrer, bool isNewWindow,
									   bool &cancelDefaultAction) {
		callFuncA(
			"onNavigationRequested",
			"(Lorg/berkelium/java/Window;Ljava/lang/String;Ljava/lang/String;Z[Z)V",
			map(win),
			map(newURL),
			map(referrer),
			isNewWindow,
			mapRw(cancelDefaultAction)
		);
		// FIXME: unmap(cancelDefaultAction)
	}

	virtual void onLoadingStateChanged(Berkelium::Window *win, bool isLoading) {
		callFunc("onLoadingStateChanged", win, isLoading);
	}

	virtual void onTitleChanged(Berkelium::Window *win, Berkelium::WideString title) {
		callFunc("onTitleChanged", win, map(title));
	}

	virtual void onTooltipChanged(Berkelium::Window *win, Berkelium::WideString text) {
		callFunc("onTooltipChanged", win, map(text));
	}

	virtual void onCrashed(Berkelium::Window *win) {
		callFunc("onCrashed", win);
	}

	virtual void onUnresponsive(Berkelium::Window *win) {
		callFunc("onUnresponsive", win);
	}

	virtual void onResponsive(Berkelium::Window *win) {
		callFunc("onResponsive", win);
	}

	virtual void onCreatedWindow(Berkelium::Window *win, Berkelium::Window *newWindow,
								 const Berkelium::Rect &initialRect) {
		// FIXME: move setDelegate call to java
		newWindow->setDelegate(this);
		callFunc("onCreatedWindow", win, newWindow, initialRect);
	}

	virtual void onWidgetCreated(Berkelium::Window *win, Berkelium::Widget *newWidget, int zIndex) {
		callFunc("onWidgetCreated", win, newWidget, zIndex);
	}

	virtual void onWidgetResize(Berkelium::Window *win, Berkelium::Widget *wid, int newWidth, int newHeight) {
		callFunc("onWidgetResize", win, wid, newWidth, newHeight);
	}

	virtual void onWidgetMove(Berkelium::Window *win, Berkelium::Widget *wid, int newX, int newY) {
		callFunc("onWidgetMove", win, wid, newX, newY);
	}

	virtual void onShowContextMenu(Berkelium::Window *win,
								   const Berkelium::ContextMenuEventArgs& args) {
		std::cout << "FIXME, NOT IMPLEMENTED: onShowContextMenu at " << args.mouseX << "," << args.mouseY;
		std::cout << std::endl;
	}

	virtual void onJavaScriptCallback(Berkelium::Window *win, void* replyMsg, Berkelium::URLString url, Berkelium::WideString funcName, Berkelium::Script::Variant *args, size_t numArgs) {
		std::cout << "FIXME, NOT IMPLEMENTED: onJavaBerkelium::ScriptCallback at URL " << url << ", "
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
		std::wcout << L"FIXME, NOT IMPLEMENTED: onRunFileChooser type " << mode << L", title " << title << L":" << std::endl;
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
		callFunc("onExternalHost", win, map(message), map(origin), map(target));
	}

private:

	void callFunc(const char* func, Berkelium::Window *win) {
		callFuncA(func, "(Lorg/berkelium/java/Window;)V", map(win));
	}

	void callFunc(const char* func, Berkelium::Window *win, bool val) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Z)V", map(win), val);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring str) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Ljava/lang/String;)V", map(win), str);
	}

	void callFunc(const char* func, Berkelium::Window *arg1, jstring arg2, jstring arg3, jstring arg4) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", map(arg1), arg2, arg3, arg4);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring arg1, jstring arg2, int arg3) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Ljava/lang/String;Ljava/lang/String;I)V", map(win), arg1, arg2, arg3);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring arg1, int arg2, bool arg3) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Ljava/lang/String;IZ)V", map(win), arg1, arg2, arg3);
	}

	void callFunc(const char* func, Berkelium::Window* arg1, Berkelium::Window* arg2, const Berkelium::Rect& arg3) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Lorg/berkelium/java/Window;Lorg/berkelium/java/Rect;)V", map(arg1), map(arg2), map(arg3));
	}

	void callFunc(const char* func, Berkelium::Window* arg1, Berkelium::Widget* arg2, int arg3) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Lorg/berkelium/java/Widget;I)V", map(arg1), map(arg2), arg3);
	}

	void callFunc(const char* func, Berkelium::Window* arg1, Berkelium::Widget* arg2, int arg3, int arg4) {
		callFuncA(func, "(Lorg/berkelium/java/Window;Lorg/berkelium/java/Widget;II)V", map(arg1), map(arg2), arg3, arg4);
	}

	void callFuncA(const char* func, const char* sig, ...) {
		va_list args;
		JNIEnv* env = Berkelium_Java_Env::get();
		jobject obj = env->NewGlobalRef(globalDelegate);
		if (obj == 0) {
			return;
		}
		jclass cls = env->GetObjectClass(obj);
		if (cls != 0) {
			jmethodID jmid = env->GetMethodID(cls, func, sig);
			if (jmid != 0) {
				va_start(args, sig);
				env->CallVoidMethodV(obj, jmid, args);
				va_end(args);
			}
		}
		env->DeleteGlobalRef(obj);
	}

	jobject map(Berkelium::Window* win) {
		return Berkelium_Java_Registry_get((jlong)win);
	}

	jobject map(Berkelium::Widget* wid) {
		return Berkelium_Java_Registry_get((jlong)wid);
	}

	jobject map(const Berkelium::Rect& rect) {
		return Berkelium_Java_Rect(rect);
	}

	jstring map(Berkelium::URLString str) {
		return Berkelium_Java_Env::get()->NewStringUTF(str.data());
	}

	jobject map(const void* data, size_t num) {
		return Berkelium_Java_Buffer(data, num);
	}

	jobject map(size_t num, const Berkelium::Rect* rects) {
		return Berkelium_Java_Rects(num, rects);
	}

	jbooleanArray mapRw(bool& val) {
		// FIXME!
		return 0;
	}

	jbooleanArray mapRw(Berkelium::WideString& val) {
		// FIXME!
		return 0;
	}

	// from http://stackoverflow.com/questions/870414/passing-double-byte-wchar-strings-from-c-to-java-via-jni
	jstring map(const Berkelium::WideString& ws) {
#if WIN32
		const wchar_t* utf16 = ws.data();
		int utf16_length = ws.length();
		int utf8_length = WideCharToMultiByte(
		  CP_UTF8,           // Convert to UTF-8
		  0,                 // No special character conversions required 
		                     // (UTF-16 and UTF-8 support the same characters)
		  utf16,             // UTF-16 string to convert
		  utf16_length,      // utf16 is NULL terminated (if not, use length)
		  NULL,              // Determining correct output buffer size
		  0,                 // Determining correct output buffer size
		  NULL,              // Must be NULL for CP_UTF8
		  NULL);             // Must be NULL for CP_UTF8
		
		if (utf8_length == 0) {
			printf("internal berkelium-java error: WideCharToMultiByte failed (1)\n");
			return 0;
		}
		
		char* utf8 = new char[utf8_length+1]; // Allocate space for UTF-8 string
		
		utf8_length = WideCharToMultiByte(
		  CP_UTF8,           // Convert to UTF-8
		  0,                 // No special character conversions required 
		                     // (UTF-16 and UTF-8 support the same characters)
		  utf16,             // UTF-16 string to convert
		  utf16_length,      // utf16 is NULL terminated (if not, use length)
		  utf8,              // UTF-8 output buffer
		  utf8_length,       // UTF-8 output buffer size
		  NULL,              // Must be NULL for CP_UTF8
		  NULL);             // Must be NULL for CP_UTF8
		
		if (utf8_length == 0) {
			printf("internal berkelium-java error: WideCharToMultiByte failed (2)\n");
			return 0;
		}
		
		utf8[utf8_length] = 0;
		jstring ret = Berkelium_Java_Env::get()->NewStringUTF(utf8);
		delete utf8;
		
		return ret;
#else
		return 0;
#endif
	}

};
