# queue-number

If you look at the project reactor docs, it states one of the reasons why you would use the library is because [Blocking can be wasteful](https://projectreactor.io/docs/core/release/reference/#_blocking_can_be_wasteful), in this little research project we test 2 implementations of a simple service, one blocking and the other non blocking and see where one is better than the other.

Here is the interface for the service. The service will generate sequential numbers for users queuing to get help at a support desk. I.E. the first user will be no. 1 in the queue, then no. 2, etc.:

```Java
public interface QueueNumberService {

 public int getNumber();

}
```

The blocking implementation looks like this:

```Java
public class QueueNumberServiceBlocking implements QueueNumberService {

 private int count = 0;

 private Object lock = new Object();

 @Override
 public int getNumber() {
  synchronized (lock) {
   return ++count;
  }
 }
}
```

And the non blocking implementation looks like this:

```Java
public class QueueNumberServiceNonBlocking implements QueueNumberService {

 private AtomicInteger count = new AtomicInteger(0);

 @Override
 public int getNumber() {
  return count.incrementAndGet();
 }
}
```

You will see in the test directory a unit test that made sure all users did get a queue number, and tested several scenarios, mostly using different batch sizes. On my 8 core machine I got the following results for a queue of 100 million users:

| Batch Size | Blocking (ms) | Non Blocking (ms) |
| ---------: | ------------: | ----------------: |
|          1 |        ★ 4672 |              5018 |
|         10 |          5091 |            ★ 1538 |
|        100 |          4381 |            ★ 1913 |
|       1000 |          4283 |            ★ 1869 |
|      10000 |          4038 |            ★ 1857 |
|     100000 |          5050 |            ★ 1878 |

In conclusion, non blocking rocks!
