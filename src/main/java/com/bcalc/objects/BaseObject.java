package com.bcalc.objects;

import com.bcalc.core.Core;
import static com.bcalc.core.Core.loggerConsole;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Базовый наследуемый класс с базовым набором методов для всех объектов
 * Hyperion
 *
 */
public abstract class BaseObject {

    String name = "";        //имя объекта
    int objectID = 0;    //objectID
    String objectType = ""; //objectType
    int groupId = 0;

    /**
     * локальный метод, возвращает тип объекта
     *
     * @return
     */
    private String getObjectTypeData(Object obj) {

        switch (obj.getClass().getName().split("\\.")[4]) {
            case "Art":
                return ((Art) obj).getObjectData();
            case "Art2":
                return ((Art) obj).getObjectData();

            default:
                loggerConsole.warn("Core.Hystorian.readObjects() unknown record " + obj.getClass().getCanonicalName());
                return "unknownClass";
        }

    }

    /**
     * возвращает частичную информаци об объекте (без коллекций)
     *
     * @return
     */
    public String getObjectData() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getObjectId()).append(Core.ppCollector.getOBJDEL());
        sb.append(this.getName()).append(Core.ppCollector.getOBJDEL());
        sb.append(this.getObjectType()).append(Core.ppCollector.getOBJDEL());
        for (Field s : this.getClass().getDeclaredFields()) {
            try {
                if (s.getType().isAssignableFrom(String.class) | s.getType().isAssignableFrom(int.class) | s.getType().isAssignableFrom(boolean.class)) {
                    s.setAccessible(true);

                    if (s.get(this) == null) {
                        sb.append("").append(Core.ppCollector.getOBJDEL());
                    } else {
                        sb.append(s.get(this)).append(Core.ppCollector.getOBJDEL());
                    }
                    s.setAccessible(false);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                loggerConsole.fatal("Base.BaseObject.getObjectData() exception ", ex);
            }
        }
        return sb.toString();
    }

    /**
     * переопределённый метод equals использующий лишь getObjectData
     *
     * anObject
     *
     * @param anObject
     * @return
     */
    @Override
    public boolean equals(Object anObject) {
        return anObject == null ? false : this.getObjectData().equalsIgnoreCase(((BaseObject) anObject).getObjectData());
    }

    /**
     * переопределённый метод hashCode использующий лишь getObjectData
     *
     * @return
     */
    @Override
    public int hashCode() {
        String key = this.getObjectData();

        int result = key.hashCode();
        result = 31 * result;
        return result;
    }

    /**
     * @return the objectID
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     *
     * @return
     */
    public int getObjectId() {
        return objectID;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * возвращает признак пустого объекта
     */
    public boolean isEmpty() {
        return this.name.equals("empty");
    }

}
