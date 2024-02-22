## Problem Statement:
Detecting if ArrayList is being used with or without emptiness check.
For example:
1. This is an unsafe usage of ArrayList:
```java
System.out.println(myList.get(0));
```
2. This is a safe usage of ArrayList:
```java
if (!myList.isEmpty()) {
  System.out.println(myList.get(1));
}
```