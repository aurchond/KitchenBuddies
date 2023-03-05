package org.utilities.database.relational;

import org.optimization.Resource;
import org.optimization.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    public static HashMap<String, List<Resource>> relationDbFunctionGetConstraints(User p_bud){
        HashMap<String, List<Resource>> constraints = new HashMap<String, List<Resource>>();

        // TODO: What happens if user doesn't have a specific constraint?
        // TODO: How do we map similar items to the same name? (i.e. drainer and strainer)
        // pot x2
        List<Resource> r1 = new ArrayList<Resource>() {{
            add(new Resource("pot", 1));
            add(new Resource("pot", 2));
        }};
        constraints.put("pot", r1);

        // spoon x1
        List<Resource> r2 = new ArrayList<Resource>() {{
            add(new Resource("spoon", 1));
        }};
        constraints.put("spoon", r2);

        // drainer x1
        List<Resource> r3 = new ArrayList<Resource>() {{
            add(new Resource("drainer", 1));
        }};
        constraints.put("drainer", r3);

        // oven x1
        List<Resource> r4 = new ArrayList<Resource>() {{
            add(new Resource("oven", 1));
        }};
        constraints.put("oven", r4);

        // 9x13 pan x1
        List<Resource> r5 = new ArrayList<Resource>() {{
            add(new Resource("9x13 pan", 1));
        }};
        constraints.put("9x13 pan", r5);

        // oven gloves x1
        List<Resource> r6 = new ArrayList<Resource>() {{
            add(new Resource("oven gloves", 1));
        }};
        constraints.put("oven gloves", r6);

        // wok x1
        List<Resource> r7 = new ArrayList<Resource>() {{
            add(new Resource("wok", 1));
        }};
        constraints.put("wok", r7);

        // spatula x2
        List<Resource> r8 = new ArrayList<Resource>() {{
            add(new Resource("spatula", 1));
            add(new Resource("spatula", 2));
        }};
        constraints.put("spatula", r8);

        // strainer x1
        List<Resource> r9 = new ArrayList<Resource>() {{
            add(new Resource("strainer", 1));
        }};
        constraints.put("strainer", r9);

        // baking sheet x1
        List<Resource> r10 = new ArrayList<Resource>() {{
            add(new Resource("baking sheet", 1));
        }};
        constraints.put("baking sheet", r10);

        // whisk x1
        List<Resource> r11 = new ArrayList<Resource>() {{
            add(new Resource("whisk", 1));
        }};
        constraints.put("whisk", r11);

        // glass measuring cup x1
        List<Resource> r12 = new ArrayList<Resource>() {{
            add(new Resource("glass measuring cup", 1));
        }};
        constraints.put("glass measuring cup", r12);

        // sheet 1x
        List<Resource> r13 = new ArrayList<Resource>() {{
            add(new Resource("sheet", 1));
        }};
        constraints.put("sheet", r13);

        // knife x1
        List<Resource> r14 = new ArrayList<Resource>() {{
            add(new Resource("knife", 1));
        }};
        constraints.put("knife", r14);

        // cutting board x1
        List<Resource> r15 = new ArrayList<Resource>() {{
            add(new Resource("cutting board", 1));
        }};
        constraints.put("cutting board", r15);

        // pan x1
        List<Resource> r16 = new ArrayList<Resource>() {{
            add(new Resource("pan", 1));
        }};
        constraints.put("pan", r16);
        return constraints;

    }
}