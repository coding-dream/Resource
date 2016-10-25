## 一个简单的动态链接库的使用说明(JNA的准备工作)
hello.h hello1.c hello2.c 是生成so文件的准备文件 

编写完毕后 
执行命令 
` gcc hello1.c hello2.c -fPIC -shared -o libhello.so`
然后会生成一个 libhello.so文件 

编写 main.c 来调用 libhello.so 

```
#include "hello.h"

int main(){
 printf("I am main");
 hello1();
 hello2();

 return 0;
}

```

生成 可执行文件 exefile.out 
`$ gcc main.c -L. -lhello -o exefile.out `  // -L  指定当前链接的so所在的目录  

执行
`./exefile.out`


编译参数解析

最主要的是GCC命令行的一个选项:
-shared 该选项指定生成动态连接库（让连接器生成T类型的导出符号表，有时候也生成弱连接W类型的导出符号），不用该标志外部程序无法连接。相当于一个可执行文件
-fPIC：表示编译为位置独立的代码，不用此选项的话编译后的代码是位置相关的所以动态载入时是通过代码拷贝的方式来满足不同进程的需要，而不能达到真正代码段共享的目的。
-L.：表示要连接的库在当前目录中
-lhello：so 库名字



LD_LIBRARY_PATH：这个环境变量指示动态连接器可以装载动态库的路径。
当然如果有root权限的话，可以修改/etc/ld.so.conf文件，然后调用 /sbin/ldconfig来达到同样的目的，不过如果没有root权限，那么只能采用输出LD_LIBRARY_PATH的方法了。


## 注意
调用动态库的时候有几个问题会经常碰到，有时，明明已经将库的头文件所在目录 通过 “-I” include进来了，库所在文件通过 “-L”参数引导，并指定了“-l”的库名，但通过ldd命令察看时，就是死活找不到你指定链接的so文件，这时你要作的就是通过修改 
LD_LIBRARY_PATH或者/etc/ld.so.conf文件来指定动态库的目录。通常这样做就可以解决库无法链接的问题了。
















