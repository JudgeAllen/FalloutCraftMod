@rem
@rem 版权所有 2015 原始作者或贡献者。
@rem
@rem 根据 Apache 许可证 2.0 版（"许可证"）获得许可；
@rem 除非遵守许可证，否则你不得使用此文件。
@rem 你可以在以下位置获取许可证副本：
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem 除非适用法律要求或书面同意，否则按
@rem "原样"分发的软件不附带任何明示或暗示的担保或条件。
@rem 请参阅许可证了解管辖权限和限制。
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Windows 的 Gradle 启动脚本
@rem
@rem ##########################################################################

@rem 为变量设置 Windows NT shell 的本地作用域
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem 此变量通常未使用
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem 解析 APP_HOME 中的 "." 和 ".." 以缩短路径。
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem 在此添加默认 JVM 选项。你也可以使用 JAVA_OPTS 和 GRADLE_OPTS 向此脚本传递 JVM 选项。
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem 查找 java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem 设置命令行

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar


@rem 执行 Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:end
@rem 结束 Windows NT shell 的变量本地作用域
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem 如果需要使用 _脚本_ 返回码而不是 _cmd.exe /c_ 返回码，请设置 GRADLE_EXIT_CONSOLE 变量！
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%GRADLE_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
