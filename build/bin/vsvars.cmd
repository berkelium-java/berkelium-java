@echo off

if not "%VS100COMNTOOLS%"=="" goto VS100
if not "%VS90COMNTOOLS%"=="" goto VS90

echo System Enviroment %%VS90COMNTOOLS%% or %%VS100COMNTOOLS%% must be set. VC installed?

exit /B 1

:VS90

call "%VS90COMNTOOLS%\vsvars32.bat"

goto end

:VS100

call "%VS100COMNTOOLS%\vsvars32.bat"

:end