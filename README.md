# util

## Table of Content

* Wrappers
 * Simple
 * Complex
* Limits
* Other Stuff

## Usage

Add the following Maven dependency

        <dependency>
            <groupId>se.eris</groupId>
            <artifactId>jtype</artifactId>
            <version>0.4.1</version>
        </dependency>

## Simple Wrappers

The most simple usage is to just extend a Wrapper class. The wrapper classes implement 
the hashCode, equals, and toString methods.

    public class Description extends StringWrapper {
    
        public Description(final String description) {
            super(description);
        }
    
    }
 
## Complex Wrappers

* DyadWrapper
* PairWrapper
* OneOfWrapper
* ...

## Limits

To get some use out of the library you can combine it with a Limit. In this example the description String 
is limited to max 1000 characters. 

    public class Description extends StringWrapper {
    
        public static final int MAX_LENGTH = 1000;
    
        private static final LimitedString LIMITED_STRING = LimitedString.init()
                .length(MAX_LENGTH).build();
    
        public Description(final String description) {
            super(LIMITED_STRING.of(description));
        }
    
    }

If no predefined limit matches your requirements you can create your own Limits easily:

    Limit<Integer> evenLimit = (item) -> (((item % 2) == 0) ? Optional.empty() : Optional.of(ValidationError.of(item + " is odd")));
    LimitedInteger even = LimitedInteger.init().limit(evenLimit).build();
    int a = even.of(2);   // a = 2;
    int b = even.of(17);  // throws ValidationException

## Other Stuff

### SOptional

A Serializable Optional until the Java community realizes its mistake and makes Optional serializable. It also takes some methods from the Guava Optional.

