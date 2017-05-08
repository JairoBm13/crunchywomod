// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsonFormatVisitors;

public enum JsonValueFormat
{
    COLOR {
        @Override
        public String toString() {
            return "color";
        }
    }, 
    DATE {
        @Override
        public String toString() {
            return "date";
        }
    }, 
    DATE_TIME {
        @Override
        public String toString() {
            return "date-time";
        }
    }, 
    EMAIL {
        @Override
        public String toString() {
            return "email";
        }
    }, 
    HOST_NAME {
        @Override
        public String toString() {
            return "host-name";
        }
    }, 
    IPV6 {
        @Override
        public String toString() {
            return "ipv6";
        }
    }, 
    IP_ADDRESS {
        @Override
        public String toString() {
            return "ip-address";
        }
    }, 
    PHONE {
        @Override
        public String toString() {
            return "phone";
        }
    }, 
    REGEX {
        @Override
        public String toString() {
            return "regex";
        }
    }, 
    STYLE {
        @Override
        public String toString() {
            return "style";
        }
    }, 
    TIME {
        @Override
        public String toString() {
            return "time";
        }
    }, 
    URI {
        @Override
        public String toString() {
            return "uri";
        }
    }, 
    UTC_MILLISEC {
        @Override
        public String toString() {
            return "utc-millisec";
        }
    };
}
