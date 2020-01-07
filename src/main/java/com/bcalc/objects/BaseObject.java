package com.bcalc.objects;

import com.gpn.hypmon.CoreObjects.Core;
import static com.gpn.hypmon.CoreObjects.Core.MONMARKER;
import static com.gpn.hypmon.CoreObjects.Core.loggerDB;
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
    String objectID = "";    //objectID
    String objectType = ""; //objectType

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
    public String getObjectId() {
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
     * локальный метод, возвращает тип объекта
     *
     * @return
     */
    private String getObjectTypeData(Object obj) {

        switch (obj.getClass().getName().split("\\.")[4]) {
            case "AccObject":
                return ((AccObject) obj).getObjectData();
            case "AppPriv":
                return ((AppPriv) obj).getObjectData();
            case "Application":
                return ((Application) obj).getObjectData();
            case "Component":
                return ((Component) obj).getObjectData();
            case "Cube":
                return ((Cube) obj).getObjectData();
            case "DimentionMember":
                return ((DimensionMember) obj).getObjectData();
            case "Form":
                return ((Form) obj).getObjectData();
            case "FormFolder":
                return ((FormFolder) obj).getObjectData();
            case "Group":
                return ((Group) obj).getObjectData();
            case "HssPriv":
                return ((HssPriv) obj).getObjectData();
            case "Role":
                return ((Role) obj).getObjectData();
            case "Rule":
                return ((Rule) obj).getObjectData();
            case "Ruleset":
                return ((Ruleset) obj).getObjectData();
            case "User":
                return ((User) obj).getObjectData();
            default:
                loggerDB.warn(MONMARKER, "Core.Hystorian.readObjects() unknown record " + obj.getClass().getCanonicalName());
                return "unknownClass";
        }

    }

    /**
     * метод возвращает компиляцию всех полей объекта
     *
     * @return
     */
    public String getFullObjectData() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbCol = new StringBuilder();
        for (Field s : this.getClass().getDeclaredFields()) {
            // используем рефлекцию и забираем значения
            if (s.getType().isAssignableFrom(HashSet.class) | s.getType().isAssignableFrom(HashMap.class)) {
                try {
                    s.setAccessible(true);
                    if (s.getType().isAssignableFrom(HashSet.class)) {
                        //сортируем по objectId
                        HashSet k = (HashSet) s.get(this);
                        sbCol.append(s.getName()).append(Core.properties.getOBJDEL());
                        if (k.size() > 0) {
                            for (Object obj : k) {
                                if (obj.getClass().isAssignableFrom(BaseObject.class)) {
                                    //sbCol.append(((BaseObject) obj).getObjectData());
                                    sbCol.append(getObjectTypeData(obj)).append(Core.properties.getCOLLDEL());
                                } else {
                                    sbCol.append(obj.toString()).append(Core.properties.getCOLLDEL()); //todoo
                                }
                                // sb.append(prefix).append(sbCol).append('\n');
                            }
                        } else {
                            sbCol.append("emptyHS");
                            // sb.append(prefix).append(sbCol).append('\n');
                        }
                    }
                    // внизапно нашли мапу.
                    if (s.getType().isAssignableFrom(HashMap.class)) {
                        HashMap<Object, Object> map = (HashMap<Object, Object>) s.get(this);
                        sbCol.append(s.getName()).append(Core.properties.getOBJDEL());
                        for (Object key : map.keySet()) {
                            // для каждого ключа в коллекции, если он обозначает коллецию
                            if (map.get(key).getClass().isAssignableFrom(HashSet.class)) {
                                // если внутри коллеция
                                HashSet colInMap = (HashSet) map.get(key);
                                //для каждого значения коллекции
                                sbCol.append(key.toString()).append(Core.properties.getCOBJDEL());
                                for (Object val : colInMap) {
                                    if (val.getClass().isAssignableFrom(BaseObject.class)) {
                                        sbCol.append(getObjectTypeData(val)).append(Core.properties.getCOLLDEL());
                                    } else {
                                        sbCol.append(val.toString()).append(Core.properties.getCOLLDEL());
                                    }
                                }

                            } else {
                                sbCol.append(key.toString()).append(Core.properties.getCOBJDEL())
                                        .append(map.get(key).toString()).append(Core.properties.getCOLLDEL());
                            }
                        }
                        if (map.isEmpty()) {
                            sbCol.append("emptyHS");
                        }
                    }
                    s.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    loggerDB.fatal(MONMARKER, "Base.BaseObject.getFullObjectData() exception ", ex);
                }
                sbCol.append(Core.properties.getVALDEL()); // разделяем каждое поле
            }
        }
        sb.append(sbCol);
        return sb.toString();
    }

    /**
     * возвращает частичную информаци об объекте (без коллекций)
     *
     * @return
     */
    public String getObjectData() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getObjectId()).append(Core.properties.getOBJDEL());
        sb.append(this.getName()).append(Core.properties.getOBJDEL());
        sb.append(this.getObjectType()).append(Core.properties.getOBJDEL());
        for (Field s : this.getClass().getDeclaredFields()) {
            try {
                if (s.getType().isAssignableFrom(String.class) | s.getType().isAssignableFrom(int.class) | s.getType().isAssignableFrom(boolean.class)) {
                    s.setAccessible(true);

                    if (s.get(this) == null) {
                        sb.append("").append(Core.properties.getOBJDEL());
                    } else {
                        sb.append(s.get(this)).append(Core.properties.getOBJDEL());
                    }
                    s.setAccessible(false);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                loggerDB.fatal(MONMARKER, "Base.BaseObject.getObjectData() exception ", ex);
            }
        }
        return sb.toString();
    }

    /**
     * переопределённый метод equals использующий лишь getObjectData
     *
     * anObject
     */
    @Override
    public boolean equals(Object anObject) {
        return anObject == null ? false : this.getObjectData().equalsIgnoreCase(((BaseObject) anObject).getObjectData());
    }

    /**
     * переопределённый метод hashCode использующий лишь getObjectData
     */
    @Override
    public int hashCode() {
        String key = this.getObjectData() + this.getFullObjectData();

        int result = key.hashCode();
        result = 31 * result;
        return result;
    }

    /**
     * возвращает признак пустого объекта
     */
    public boolean isEmpty() {
        return this.name.equals("empty");
    }

}
