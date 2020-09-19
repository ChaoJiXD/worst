package xyz.WorstClient.api.events.world;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
  byte value() default 2;
}


/* Location:              G:\历史遗留\AzureWare.jar!\com\darkmagician6\eventapi\EventTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */