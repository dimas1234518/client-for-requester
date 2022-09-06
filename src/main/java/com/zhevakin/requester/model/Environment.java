package com.zhevakin.requester.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Environment {

    private String name;
    private List<Variable> variables;
    private final String id;
    private static final String beginVariable = "\\{";
    private static final String endVariable = "\\}";

    public Environment(String name, List<Variable> variables) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.variables = variables;
    }

    public Environment(String id, String name, List<Variable> variables) {
        this.id = id;
        this.name = name;
        this.variables = variables;
    }

    public Environment() {
        id = UUID.randomUUID().toString();
    }

    public Environment(String id) {
        this.id = id;
    }

    public String getId() { return id;}

    public void updateName (String name) { this.name = name; }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void addVariables (ArrayList<Variable> variables) {
        this.variables.addAll(variables);
    }

    public void addVariable(Variable variable, int index) { variables.add(index, variable); }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public List<Variable> getVariables() { return variables; }

    public String getFullText (String input) {

        // https://rappdef.gloria.aaanet.ru:{Test}/{Test2}/{Test3}
        char[] arrInput = input.toCharArray();
        StringBuilder fullText = new StringBuilder();

        // Собираем переменную полностью с учетом ${}
        // При поиске через stream() убираем просто их
        // Если не убираются, то
        var partInput = input.split(beginVariable);

        // https://${
        // perem1
        // }
        // /${
        // perem2
        // }/sfldg
        //
        for (var part : partInput)
        {
            var temp = part.split(endVariable);

            for (var t : temp)
            {

                var tempVariable = (beginVariable + t + endVariable).replaceAll("\\\\","");

                int index = input.indexOf(tempVariable);
                if (index ==-1) {
                    fullText.append(t);
                    continue;
                }

                var found = variables
                        .stream()
                        .filter(v -> v.toString().equals(t))
                        .findFirst()
                        .orElse(null);
                if (found == null)
                    fullText.append(tempVariable);
                else
                    fullText.append(found.getValue());
            }
        }


        // Делим на массивы. Сначала по beginVariable потом по endVariable
        // После второго разделения находим нужную переменную
        // Цикл
        // получаем ее значение
        // Ищем в ArrayList
        // Если находим, то подставляем
        // КонецЦикла
        // Потом производим сборку текста
        // Возвращаем

        return fullText.toString();
    }

    @Override
    public String toString() { return name; }

}
