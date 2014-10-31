javabean_to_string
==================

## What is it
This is a tool to generate a string representation of a JavaBean in such way that

* There is no maintenance cost for the developer of the bean
* The string generation is optimized in performance 


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
