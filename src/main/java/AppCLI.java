import ba.rpr.business.MicronutrientManager;
import ba.rpr.business.PresenceManager;
import ba.rpr.business.SourceManager;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import org.apache.commons.cli.*;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.WordUtils;


import java.util.List;

public class AppCLI {
    final static Option add = new Option("a", "add", false, "Add element, specify with option");
    final static Option update = new Option("u", "update", false, "Update element, specify with option");
    final static Option delete = new Option("d", "delete", false, "Delete element, specify with option");
    final static Option getAll = new Option("g", "getall", false, "Get all elements, specify with option");
    final static Option sourcesOfMicronutrient = new Option("som", "sources-of-micronutrient", true,
            "Show sources of micronutrient");
    final static Option micronutrientInSource = new Option("mis", "micronutrients-in-source", true,
            "Show micronutrients in source");

    final static Option source = Option.builder().option("s").longOpt("source").hasArgs().optionalArg(true).
            desc("Source arguments for chosen action.\n" +
                    "Arguments that need to passed based on action:\n" +
                    "Add: name\n" +
                    "Update: id new_name\n" +
                    "Delete: id\n").build();
    final static Option micronutrient = Option.builder().option("m").longOpt("micronutrient").hasArgs().
            optionalArg(true).desc("Micronutrient arguments for chosen action.\n" +
                    "Arguments that need to passed based on action:\n" +
                    "Add: name role (vitamin OR mineral)\n" +
                    "Update: id new_name new_role (vitamin OR mineral)\n" +
                    "Delete: id\n").build();
    final static Option presence = Option.builder().option("p").longOpt("presence").hasArgs().
            optionalArg(true).desc("Presence arguments for chosen action.\n" +
                    "Arguments that need to passed based on action:\n" +
                    "Add: micronutrient_name source_name amount\n" +
                    "Update: id new_micronutrient_name new_source_name new_amount\n" +
                    "Delete: id\n").build();

    private static final SourceManager sourceManager = new SourceManager();
    private static final MicronutrientManager micronutrientManager = new MicronutrientManager();
    private static final PresenceManager presenceManager = new PresenceManager();
    private static void add(CommandLine line) throws DaoException, NumberFormatException {
        if(line.hasOption(source)) {
            String[] params = line.getOptionValues(source);
            if(params == null || params.length != 1) throw new DaoException("Number of parameters should be 1");
            Source s = new Source(-1, params[0]);
            sourceManager.add(s);
        } else if(line.hasOption(micronutrient)) {
            String[] params = line.getOptionValues(micronutrient);
            if(params == null || params.length != 3) throw new DaoException("Number of parameters should be 3");
            boolean isVitamin;
            if(params[2].equals("vitamin")) isVitamin = true;
            else if(params[2].equals("mineral")) isVitamin = false;
            else throw new DaoException("Enter vitamin or mineral");
            Micronutrient m = new Micronutrient(-1, params[0],
                    params[1], isVitamin);
            micronutrientManager.add(m);
        } else if(line.hasOption(presence)) {
            String[] params = line.getOptionValues(presence);
            if(params == null || params.length != 3) throw new DaoException("Number of parameters should be 3");
            Presence p = new Presence(-1, micronutrientManager.searchByName(params[0]),
                    sourceManager.searchByName(params[1]), Double.parseDouble(params[2]));
            presenceManager.add(p);
        } else {
            throw new DaoException("Domain type needed for given action.");
        }
        System.out.println("Element successfully added.");
    }

    private static void update(CommandLine line) throws DaoException, NumberFormatException {
        if(line.hasOption(source)) {
            String[] params = line.getOptionValues(source);
            if(params == null || params.length != 2) throw new DaoException("Number of parameters should be 2");
            int id = Integer.parseInt(params[0]);
            Source s = new Source(-1, params[1]);
            sourceManager.update(id, s);
        } else if(line.hasOption(micronutrient)) {
            String[] params = line.getOptionValues(micronutrient);
            if(params == null || params.length != 4) throw new DaoException("Number of parameters should be 4");
            int id = Integer.parseInt(params[0]);
            boolean isVitamin;
            if(params[3].equals("vitamin")) isVitamin = true;
            else if(params[3].equals("mineral")) isVitamin = false;
            else throw new DaoException("Enter vitamin or mineral");
            Micronutrient m = new Micronutrient(-1, params[1],
                    params[2], isVitamin);
            micronutrientManager.update(id, m);
        } else if(line.hasOption(presence)) {
            String[] params = line.getOptionValues(presence);
            if(params == null || params.length != 4) throw new DaoException("Number of parameters should be 4");
            int id = Integer.parseInt(params[0]);
            Presence p = new Presence(-1, micronutrientManager.searchByName(params[1]),
                    sourceManager.searchByName(params[2]), Double.parseDouble(params[3]));
            presenceManager.update(id, p);
        } else {
            throw new DaoException("Domain type needed for given action.");
        }
        System.out.println("Element successfully updated.");
    }

    private static void delete(CommandLine line) throws DaoException, NumberFormatException {
        if(line.hasOption(source)) {
            String[] params = line.getOptionValues(source);
            if(params == null || params.length != 1) throw new DaoException("Number of parameters should be 1");
            int id = Integer.parseInt(params[0]);
            sourceManager.delete(id);
        } else if(line.hasOption(micronutrient)) {
            String[] params = line.getOptionValues(micronutrient);
            if(params == null || params.length != 1) throw new DaoException("Number of parameters should be 1");
            int id = Integer.parseInt(params[0]);
            micronutrientManager.delete(id);
        } else if(line.hasOption(presence)) {
            String[] params = line.getOptionValues(presence);
            if(params == null || params.length != 1) throw new DaoException("Number of parameters should be 1");
            int id = Integer.parseInt(params[0]);
            presenceManager.delete(id);
        } else {
            throw new DaoException("Domain type needed for given action.");
        }
        System.out.println("Element successfully deleted.");
    }

    private static void getAll(CommandLine line) throws DaoException {
        if(line.hasOption(source)) {
            String[] params = line.getOptionValues(source);
            if(params != null && params.length != 0) throw new DaoException("Number of parameters should be 0");
            printNicelySources(sourceManager.getAll());
        } else if(line.hasOption(micronutrient)) {
            String[] params = line.getOptionValues(micronutrient);
            if(params != null && params.length != 0) throw new DaoException("Number of parameters should be 0");
            printNicelyMicronutrients(micronutrientManager.getAll());
        } else if(line.hasOption(presence)) {
            String[] params = line.getOptionValues(presence);
            if(params != null && params.length != 0) throw new DaoException("Number of parameters should be 0");
            printNicelyPresences(presenceManager.getAll());
        } else {
            throw new DaoException("Domain type needed for given action.");
        }
    }

    private static void printNicelySources(List<Source> sources) {
        if(sources.isEmpty()) System.out.println("There are no sources in database");
        else {
            System.out.printf("%-5s%-50s\n", "Id", "Name");
            for (Source source : sources)
                System.out.printf("%-5d%-50s\n", source.getId(), source.getName());
        }
    }
    private static void printNicelyMicronutrients(List<Micronutrient> micronutrients) {
        if(micronutrients.isEmpty()) System.out.println("There are no micronutrients in database");
        else {
            System.out.printf("%-5s%-50s%-10s%s\n", "Id", "Name", "Type", "Role");
            for (Micronutrient micronutrient : micronutrients) {
                String type = micronutrient.isVitamin() ? "Vitamin" : "Mineral";
                System.out.printf("%-5d%-50s%-10s", micronutrient.getId(), micronutrient.getName(), type);
                String role = WordUtils.wrap(micronutrient.getRole(), 25, "\n", false);
                String[] words = role.split("\n");
                for (int i = 0; i < words.length; i++) {
                    if (i == 0) System.out.printf("%s\n", words[i]);
                    else System.out.printf("%s\n", new TextStringBuilder().appendPadding(65, ' ').
                            append(words[i]).build());
                }
            }
        }
    }
    private static void printNicelyPresences(List<Presence> presences) {
        if(presences.isEmpty()) System.out.println("There are no presences in database");
        else {
            System.out.printf("%-5s%-50s%-50s%-10s\n", "Id", "Micronutrient", "Source", "Amount (in 100g)");
            for (Presence presence : presences)
                System.out.printf("%-5d%-50s%-50s%f mg\n", presence.getId(), presence.getMicronutrient().getName(),
                        presence.getSource().getName(), presence.getAmount());
        }
    }

    public static void main(String[] args) {
        OptionGroup actions = new OptionGroup();
        actions.addOption(add);
        actions.addOption(update);
        actions.addOption(delete);
        actions.addOption(getAll);
        actions.addOption(sourcesOfMicronutrient);
        actions.addOption(micronutrientInSource);
        actions.setRequired(true);
        OptionGroup domain = new OptionGroup();
        domain.addOption(source);
        domain.addOption(micronutrient);
        domain.addOption(presence);
        domain.setRequired(false);
        Options options = new Options();
        options.addOptionGroup(actions);
        options.addOptionGroup(domain);
        if(args.length == 0) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.setOptionComparator(null);
            helpFormatter.setWidth(90);
            helpFormatter.printHelp("myapp",
                    "Execute CRUD functions or search presences by micronutrient or source name",
                    options, "", true);
            System.exit(-1);
        }
        try {
            DefaultParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);
            if(line.hasOption(add)) add(line);
            else if(line.hasOption(update)) update(line);
            else if(line.hasOption(delete)) delete(line);
            else if(line.hasOption(getAll)) getAll(line);
            else if(line.hasOption(sourcesOfMicronutrient)) {
                if(line.getOptions().length != 1)
                    throw new Exception("When using -som option you cannot have other options.");
                String[] params = line.getOptionValues(sourcesOfMicronutrient);
                Micronutrient m = micronutrientManager.searchByName(params[0]);
                printNicelyPresences(presenceManager.sourcesOfMicronutrient(m));
            }
            else if(line.hasOption(micronutrientInSource)) {
                if(line.getOptions().length != 1)
                    throw new Exception("When using -mis option you cannot have other options.");
                String[] params = line.getOptionValues(micronutrientInSource);
                Source s = sourceManager.searchByName(params[0]);
                printNicelyPresences(presenceManager.micronutrientsInSource(s));
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}





















