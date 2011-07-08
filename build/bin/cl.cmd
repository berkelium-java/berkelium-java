@echo off

call %BASEDIR%/build\bin\vsvars.cmd

echo cl %ARGS%
"%VCINSTALLDIR%\bin\cl.exe" %ARGS%