Assumption:
    1. Only support default log format
    2. Only support for v2

How to compile and run:
    1. Install Java 23 in the machine
    2. Get inside src folder
    3. Run `javac Main.java`, this will compile and generate a `Main.class` file.
    4. Run command `java Main <log-file> <tag-file>`. Here `<log-file>` is the file for log file,
        and `<tag-file>` is the file for tag mapping.
        For example `java Main ./test/log ./test/lookup.csv`.
    5. Check the file generated inside output folder.
