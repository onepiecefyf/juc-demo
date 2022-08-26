### 线程虚假唤醒问题

#### 原因分析
1、创建资源类Share  
属性：int count = 1  
方法：incr 加1 ｜ decr 减1

2、为incr方法上锁，判断count != 0，等待，否则 加1，并唤醒其他线程

3、为decr方法上锁，判断count != 1，等待，否则 减1，并唤醒其他线程

4、在main方法中创建两个线程分别调用incr和decr实现加减1操作，打印交替为1和0

5、在main方法中创建四个线程分别调用incr和decr实现加减1操作，打印结果并非为0和1

#### 解决方案

主要原因就是线程唤醒之后，会在被唤醒的地方继续执行，如果使用if判断，则不会再次执行if判断，而直接执行加减操作，
解决的方案就是使用while。



### 写时复制原理
写时复制在JAVA中的应用
> 在并发访问的情景下，当需要修改JAVA中Containers的元素时，不直接修改该容器，而是先复制一份副本，在副本上进行修改。修改完成之后，将指向原来容器的引用指向新的容器(副本容器)。

写时复制在linux中应用
> Linux通过Copy On Write技术极大地减少了Fork的开销。文件系统通过Copy On Write技术一定程度上保证数据的完整性。数据库服务器也一般采用了写时复制策略，为用户提供一份snapshot。


#### Vector & Collections.synchronizedList(new ArrayList()) & CopyOnWriteArrayList

Vector：在其内部每个方法都添加了synchronized关键字,保证整体的方法原子化。

Collections.synchronizedList： 不是在每个方法的声明处添加关键字synchronized，而是在方法的内部添加，保证整体执行的原子性。

CopyOnWriteArrayList：实现写时复制技术，保证并发读，写时加锁复制一份，在备份写，完成之后在合并。













