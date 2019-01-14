javabean_to_string
==================

## What is it
This is a tool to generate a string representation of a JavaBean in such way that

 * There is no maintenance cost for the developer of the bean
 * The string generation is optimized in performance 




### The problem
During the life of an application, JavaBeans (POJOs, DTOs, etc) evolve: some fieds are added, some removed, some change type. For every of this change, _we'd like to update the String representation of the object to reflect the updated fields_. This might be important if you're logging the beans. But very often the dev in charge, forgets to amend the toString() method. 

A simple approach is to delegate the generation of the String representation to a separate objects. It can use Reflection to go through the fields and generate the string representation. But a Reflection-based implementation of toString() is not very efficient.


### What javabean_to_string does

* Given a JavaBean class MyBean
* when called to generate the string representation of an instance of MyBean
  *  the first time, goes through reflection, and generates, compile a toString() implementation specific to that class
  * the following times, it reuses the generated implementation.

The generated to String, won't be less efficient than a manual implementation.

## Usage

### As implementation of the bean's toString()
    class MyBean {
      private BeanToString toStringGenerator = new BeanToString();
      
      ...
      
      public String toString()
      {
        return toStringGenerator.toString(this);
      }
    }

### As string generator
    BeanToString generator = new BeanToString();
    
    MyBean bean = .....
    
    String representation = generator.toString(bean);
