#include "berkelium-java.h"

class JavaWindowDelegateProxy : public Berkelium::WindowDelegate {
private:
	jobject globalDelegate;
	
public:
	JavaWindowDelegateProxy(JNIEnv* env, jobject localDelegate) {
		globalDelegate = env->NewGlobalRef(localDelegate);
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
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onPaint,
			java->map(wini),
			java->map(bitmap_in, 4 * bitmap_rect.width() * bitmap_rect.height()),
			java->map(bitmap_rect),
			java->map(num_copy_rects, copy_rects),
			dx,
			dy,
			java->map(scroll_rect)
		);
	}

	virtual void onAddressBarChanged(Berkelium::Window *win, Berkelium::URLString newURL) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onAddressBarChanged, java->map(win), java->map(newURL));
	}

	virtual void onStartLoading(Berkelium::Window *win, Berkelium::URLString newURL) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onStartLoading, java->map(win), java->map(newURL));
	}
	
	virtual void onLoad(Berkelium::Window *win) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onLoad, java->map(win));
	}
	
	virtual void onCrashedWorker(Berkelium::Window *win) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onCrashedWorker, java->map(win));
	}

	virtual void onCrashedPlugin(Berkelium::Window *win, Berkelium::WideString pluginName) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onCrashedPlugin, java->map(win), java->map(pluginName));
	}

	virtual void onProvisionalLoadError(Berkelium::Window *win, Berkelium::URLString url,
										int errorCode, bool isMainFrame) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onProvisionalLoadError, java->map(win), java->map(url), errorCode, isMainFrame);
	}

	virtual void onConsoleMessage(Berkelium::Window *win, Berkelium::WideString message,
								  Berkelium::WideString sourceId, int line_no) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onConsoleMessage, java->map(win), java->map(message), java->map(sourceId), line_no);
	}

	virtual void onScriptAlert(Berkelium::Window *win, Berkelium::WideString message,
							  Berkelium::WideString defaultValue, Berkelium::URLString url,
							  int flags, bool &success, Berkelium::WideString &value) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onScriptAlert,
			java->map(win),
			java->map(message),
			java->map(defaultValue),
			java->map(url),
			flags,
			java->mapRw(success),
			java->mapRw(value)
		);
		// FIXME: unmap(success)
		// FIXME: unmap(value)
	}

	virtual void onNavigationRequested(Berkelium::Window *win, Berkelium::URLString newURL,
									   Berkelium::URLString referrer, bool isNewWindow,
									   bool &cancelDefaultAction) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onNavigationRequested,
			java->map(win),
			java->map(newURL),
			java->map(referrer),
			isNewWindow,
			java->mapRw(cancelDefaultAction)
		);
		// FIXME: unmap(cancelDefaultAction)
	}

	virtual void onLoadingStateChanged(Berkelium::Window *win, bool isLoading) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onLoadingStateChanged, java->map(win), isLoading);
	}

	virtual void onTitleChanged(Berkelium::Window *win, Berkelium::WideString title) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onTitleChanged, java->map(win), java->map(title));
	}

	virtual void onTooltipChanged(Berkelium::Window *win, Berkelium::WideString text) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onTooltipChanged, java->map(win), java->map(text));
	}

	virtual void onCrashed(Berkelium::Window *win) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onCrashed, java->map(win));
	}

	virtual void onUnresponsive(Berkelium::Window *win) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onUnresponsive, java->map(win));
	}

	virtual void onResponsive(Berkelium::Window *win) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onResponsive, java->map(win));
	}

	virtual void onCreatedWindow(Berkelium::Window *win, Berkelium::Window *newWindow,
								 const Berkelium::Rect &initialRect) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		// FIXME: move setDelegate call to java
		newWindow->setDelegate(this);
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onCreatedWindow, java->map(win), java->map(newWindow), java->map(initialRect));
	}

	virtual void onWidgetCreated(Berkelium::Window *win, Berkelium::Widget *newWidget, int zIndex) {
	}

	virtual void onWidgetResize(Berkelium::Window *win, Berkelium::Widget *wid, int newWidth, int newHeight) {
	}

	virtual void onWidgetMove(Berkelium::Window *win, Berkelium::Widget *wid, int newX, int newY) {
	}

	virtual void onShowContextMenu(Berkelium::Window *win,
								   const Berkelium::ContextMenuEventArgs& args) {
	}

	virtual void onJavascriptCallback(Berkelium::Window *win, void* replyMsg, Berkelium::URLString url, Berkelium::WideString funcName, Berkelium::Script::Variant *args, size_t numArgs) {
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onJavascriptCallback, win, java->map(url), java->map(funcName));
		/*
		std::cout << "FIXME, NOT IMPLEMENTED: onJavaBerkelium::ScriptCallback at URL " << url << ", "
				  << (replyMsg?"synchronous":"async") << std::endl;
		std::wcout << L"	Function name: " << funcName << std::endl;
		*/
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
		Java* java = Java::getJava();
		if(java == NULL) return;
		java->env->CallVoidMethod(globalDelegate, java->_call.WindowDelegate_onExternalHost, java->map(win), java->map(message), java->map(origin), java->map(target));
	}
};
