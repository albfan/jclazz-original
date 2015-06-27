package ru.andrew.jclazz.apps.tester.suite;

public class JavaVersion
{
    private String currentVersion;
    private String nextVersion;

    public JavaVersion(String currentVersion, String nextVersion)
    {
        this.currentVersion = currentVersion;
        this.nextVersion = nextVersion;
    }

    public String getCurrentVersion()
    {
        return currentVersion;
    }

    public String getNextVersion()
    {
        return nextVersion;
    }
}
