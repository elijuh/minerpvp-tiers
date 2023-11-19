package dev.elijuh.minerpvp.util;

import com.comphenix.protocol.injector.PacketConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Predicate;

@UtilityClass
public class ReflectionUtil {

    private final String NM_PACKAGE = "net.minecraft";
    public final String OBC_PACKAGE = "org.bukkit.craftbukkit";
    public final String NMS_PACKAGE = NM_PACKAGE + ".server";

    public final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(OBC_PACKAGE.length() + 1);

    private final MethodType VOID_METHOD_TYPE = MethodType.methodType(void.class);
    private final boolean NMS_REPACKAGED = optionalClass(NM_PACKAGE + ".network.protocol.Packet").isPresent();

    private volatile Object theUnsafe;

    public Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRepackaged() {
        return NMS_REPACKAGED;
    }

    public String nmsClassName(String post1_17package, String className) {
        if (NMS_REPACKAGED) {
            String classPackage = post1_17package == null ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package;
            return classPackage + '.' + className;
        }
        return NMS_PACKAGE + '.' + VERSION + '.' + className;
    }

    public Class<?> nmsClass(String post1_17package, String className) {
        try {
            return Class.forName(nmsClassName(post1_17package, className));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Class<?>> nmsOptionalClass(String post1_17package, String className) {
        return optionalClass(nmsClassName(post1_17package, className));
    }

    public String obcClassName(String className) {
        return OBC_PACKAGE + '.' + VERSION + '.' + className;
    }

    public Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(obcClassName(className));
    }

    public Optional<Class<?>> obcOptionalClass(String className) {
        return optionalClass(obcClassName(className));
    }

    public Optional<Class<?>> optionalClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public Object enumValueOf(Class<?> enumClass, String enumName) {
        return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
    }

    public Object enumValueOf(Class<?> enumClass, String enumName, int fallbackOrdinal) {
        try {
            return enumValueOf(enumClass, enumName);
        } catch (IllegalArgumentException e) {
            Object[] constants = enumClass.getEnumConstants();
            if (constants.length > fallbackOrdinal) {
                return constants[fallbackOrdinal];
            }
            throw e;
        }
    }

    public Class<?> innerClass(Class<?> parentClass, Predicate<Class<?>> classPredicate) throws ClassNotFoundException {
        for (Class<?> innerClass : parentClass.getDeclaredClasses()) {
            if (classPredicate.test(innerClass)) {
                return innerClass;
            }
        }
        throw new ClassNotFoundException("No class in " + parentClass.getCanonicalName() + " matches the predicate.");
    }

    public PacketConstructor findPacketConstructor(Class<?> packetClass, MethodHandles.Lookup lookup) throws Exception {
        try {
            MethodHandle constructor = lookup.findConstructor(packetClass, VOID_METHOD_TYPE);
            return constructor::invoke;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            // try below with Unsafe
        }

        if (theUnsafe == null) {
            synchronized (ReflectionUtil.class) {
                if (theUnsafe == null) {
                    Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                    Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
                    theUnsafeField.setAccessible(true);
                    theUnsafe = theUnsafeField.get(null);
                }
            }
        }

        MethodType allocateMethodType = MethodType.methodType(Object.class, Class.class);
        MethodHandle allocateMethod = lookup.findVirtual(theUnsafe.getClass(), "allocateInstance", allocateMethodType);
        return () -> allocateMethod.invoke(theUnsafe, packetClass);
    }

    @FunctionalInterface
    public interface PacketConstructor {
        Object invoke() throws Throwable;
    }
}

