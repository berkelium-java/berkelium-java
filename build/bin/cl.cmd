@echo off

call build\bin\vsvars.cmd

echo cl %CL%
"%VCINSTALLDIR%\bin\cl.exe"