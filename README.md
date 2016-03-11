# util

## Usage

The most simple usage is to just extend a Wrapper class. The wrapper classes implement 
the hashCode, equals, and toString methods.

    public class Description extends StringWrapper {
    
        public Description(final String description) {
            super(description);
        }
    
    }

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

If there is no predefined limit you can create your own easily