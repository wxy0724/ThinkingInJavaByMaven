package com.lovetogether.thinkinjava.chapter.x;

public class MNA {
    private void f(){}
    class A {
        private void g() {}
        public class B {
            void h(){
                g();
                f();
            }
        }
    }
}