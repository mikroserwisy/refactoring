package pl.sages.platform.projects.commands;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity
@Data
public class Command {

    private static final Pattern ARGUMENTS = Pattern.compile("\"([^\"]*)\"|(\\S+)");
    private static final String SEPARATOR = " &{2} ";

    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String value;
    @Column(name = "expected_result", length = 100_000)
    private String expectedResult;

    public List<String[]> parse() {
        return Arrays.stream(value.split(SEPARATOR))
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private String[] parse(String instruction) {
        Matcher matcher = ARGUMENTS.matcher(instruction);
        List<String> arguments = new ArrayList<>();
        while (matcher.find()) {
            arguments.add(matcher.group());
        }
        return arguments.toArray(new String[]{});
    }

    public boolean hasId(Long commandId) {
        return id != null && id.equals(commandId);
    }

    public CommandExecutionStatus getStatus(String commandOutput) {
        if (expectedResult != null && expectedResult.equals(commandOutput)) {
            return CommandExecutionStatus.SUCCESS;
        } else {
            return CommandExecutionStatus.NONE;
        }
    }

}
