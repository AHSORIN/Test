$windowHandle = WinGetHandle("Open")
        WinActivate($windowHandle)

		ControlSetText("Open", "", "[CLASS:Edit; INSTANCE:1]", $CmdLine[1])
	    ControlClick("Open", "","[CLASS:Button; INSTANCE:1]")