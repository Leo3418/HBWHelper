# Contribution Guide

We really appreciate your help to make this mod better. Before you start
contributing to HBW Helper, please spend a few minutes reading this guide,
since it contains some important information.

## Table of Contents

- [Workspace Setup Instructions](#workspace-setup-instructions)
  - [Downloading Source Code of This Mod](#downloading-source-code-of-this-mod)
  - [Configuring Your Project](#configuring-your-project)
  - [Testing This Mod from Your IDE](#testing-this-mod-from-your-ide)
- [Programming Guidelines](#programming-guidelines)
  - [Code Style](#code-style)
    - [Import Statements](#import-statements)
    - [Formatting](#formatting)
  - [Documentation Style](#documentation-style)
  - [Compatibility Concern](#compatibility-concern)
- [Patch Instructions](#patch-instructions)
  - [Description of Branches](#description-of-branches)
  - [Testing for Multiple Client Versions](#testing-for-multiple-client-versions)
    - [Reconfiguring Your Project](#reconfiguring-your-project)
    - [Applying Your Change onto Another Branch](#applying-your-change-onto-another-branch)
- [Build Instructions](#build-instructions)
- [Ticket Submitting Guidelines](#ticket-submitting-guidelines)
  - [Pull Request Guidelines](#pull-request-guidelines)
  - [Issue Guidelines](#issue-guidelines)

## Workspace Setup Instructions

The following instructions assume you have correctly configured the following
components on your computer:

- Java SE Development Kit 8 (a.k.a. JDK 8)

  - Please do not use JDK 9 or newer releases since they are not supported by
    Minecraft Forge.

  - Oracle is now requiring an account for downloading Oracle JDK 8. If you do
    not want to use an account, you can install OpenJDK 8 instead. It is
    available from most package managers; there are also community OpenJDK
    builds that can be downloaded, like
    [ojdkbuild](https://github.com/ojdkbuild/ojdkbuild).

- An Integrated Development Environment (a.k.a. IDE) for Java SE

  We recommend [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/) and
  [Eclipse](https://eclipse.org) because they are officially supported by
  Minecraft Forge Mod Development Kit.

  If you concern about which edition of IntelliJ IDEA you should use, the
  Community Edition is enough for development of a Minecraft Forge Mod.

- A [Git](https://git-scm.com/) client, either standalone or bundled with the
  [GitHub Desktop](https://desktop.github.com) client, for submitting your
  contribution to us

### Downloading Source Code of This Mod

1. [Fork](https://help.github.com/articles/fork-a-repo/#fork-an-example-repository)
   this repository.

2. [Clone](https://help.github.com/articles/fork-a-repo/#step-2-create-a-local-clone-of-your-fork)
   **your own fork** to a folder on your computer where you want to store your
   code.

### Configuring Your Project

#### Configuring on `1.8.9`, `1.9-1.10.2`, or `1.11-1.12.2` Branch

1. Bring up a shell from a terminal; if you are on Windows, you can open up a
   Command Prompt window instead. Navigate to the folder where you have cloned
   the repository.

2. Execute the following command from your shell:
   ```
   ./gradlew setupDecompWorkspace
   ```

   Or, if you are using Command Prompt, execute:
   ```
   gradlew setupDecompWorkspace
   ```

   Then, wait for the process to complete.

3. Perform this step only if you are using **Eclipse** for development:

   Execute:
   ```
   ./gradlew eclipse
   ```

   Or, in Command Prompt, execute:
   ```
   gradlew eclipse
   ```

4. Import the folder storing your fork in your IDE.

   On Eclipse, please use the "Gradle > Existing Gradle Project" import wizard.

   On IntelliJ IDEA, make sure you either select `build.gradle` in the folder
   or choose "Gradle" as the project model if you select the folder itself.

5. Generate run configurations. Although this step is not required, it is
   recommended.

   - If you are using **IntelliJ IDEA** for development:

     - Close this project in IntelliJ IDEA if it is opened.

     - Execute:
       ```
       ./gradlew genIntelliJRuns
       ```

       Or, in Command Prompt, execute:
       ```
       gradlew genIntelliJRuns
       ```

    - Then, reopen the project, and you can see run configurations.

  - If you are using **Eclipse** for development:

    - Select "Run > Run Configurations..." from menu.

    - Select "Java Application" on the left panel, and select
      "HBWHelper_Client".

    - Click on "Run". In the future, you can directly start this configuration
      from menu.

#### Configuring on All Other Branches, Including `dev`

1. Bring up a shell from a terminal; if you are on Windows, you can open up a
   Command Prompt window instead. Navigate to the folder where you have cloned
   the repository.

2. Perform this step only if you are using **Eclipse** for development:

   Execute the following command from your shell:
   ```
   ./gradlew genEclipseRuns
   ```

   If you are using Command Prompt, execute this instead:
   ```
   gradlew genEclipseRuns
   ```

3. Import the folder storing your fork in your IDE.

   On Eclipse, please use the "Gradle > Existing Gradle Project" import wizard.

   On IntelliJ IDEA, make sure you either select `build.gradle` in the folder
   or choose "Gradle" as the project model if you select the folder itself.

4. Generate run configurations. Although this step is not required, it is
   recommended.

   - If you are using **IntelliJ IDEA** for development:

     - Close this project in IntelliJ IDEA if it is opened.

     - Execute:
       ```
       ./gradlew genIntelliJRuns
       ```

       Or, in Command Prompt, execute:
       ```
       gradlew genIntelliJRuns
       ```

     - Then, reopen the project, and you can see run configurations.

   - If you are using **Eclipse** for development:

     - Select "Run > Run Configurations..." from menu.

     - Select "Java Application" on the left panel, and select "runClient".

     - Click on "Run". In the future, you can directly start this configuration
       from menu.

### Testing This Mod from Your IDE

You can use the run configuration in your IDE to start a Minecraft client and
test this mod. The mod will be compiled from the source code in the current
project. This makes testing any changes to the mod easier since you do not need
to build a JAR artifact and copy it to your own Minecraft game directory every
time.

However, the default run configuration does not use a Mojang account, so you
cannot log into Hypixel and test this mod. In order to log in, you need to
specify the following *program arguments* in the run configuration:

```
--username=<email> --password=<password>
```

Replace `<email>` with the email address of your Mojang account and
`<password>` with your password. Note that **the password will be saved in
clear text**.

Testing this mod with the IDE run configuration **cannot** fully replace
testing with a standalone Minecraft client. Before committing your changes, you
are strongly advised to [build a JAR artifact](#build-instructions), install it
to a standalone Minecraft client, and perform a final test.

[Return to top](#table-of-contents)

## Programming Guidelines

A free software project welcomes everyone's contribution. We understand that
different developers have distinct programming habits, and this sometimes
reflects in the code they write. However, please try your best to maintain a
consistency in code style in this project.

### Code Style

Our suggested style is very similar to those in
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
There are some exceptions when we should not apply the style described in that
guide. These exceptions include, but are not limited to, the following.

#### Import Statements

- Wildcard imports may be allowed **only** when more than 4 classes from the
  same package are imported.

- Put import statements of Java built-in packages and classes in a separate
  block, and leave one blank line before that block.

#### Formatting

- Use **4 spaces** for each block indentation.

- Try to limit a line's column to 80 characters.

- Indent continuation lines **8 spaces**.

- For short array initialization statements, **do not** use the "block-like"
  format.

Most styles described above are in default code style settings in IntelliJ
IDEA. For those that are not in the default settings, you can create a custom
code style profile for this project and modify it to reflect those styles.

### Documentation Style

We use Javadoc for documentation comments in this project. When writing Javadoc
comments, please follow
[this guide](http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html)
from Oracle. However, there are some other things to notice in addition to
Oracle's documentation:

- Use `@code` tag instead of `<code></code>`, unless you are in a case where
  you must use an [HTML
  entity](https://www.w3schools.com/html/html_entities.asp), such as including
  `@` in a sample code.

- Surround a standalone sample code block with `<blockquote><pre>` and
  `</pre></blockquote>` tags.

- **Do not** align parameter descriptions, since if we do so, we may need to
  realign them if we add, change, or remove any parameter.

- Indent a tag's continuation lines **8 spaces**.

### Compatibility Concern

We try to let HBW Helper support as many Minecraft client versions as
possible. To help us accomplish this goal, please try to use a *universal*
implementation. A universal implementation means that it can be ported to other
Minecraft client versions with no to a little modification.

For example, `Minecraft.getMinecraft().thePlayer` in 1.10.2 and previous
versions was changed to `Minecraft.getMinecraft().player` in 1.11. Because we
only need to modify the field's name to make the code compatible with 1.11,
this can be considered as a universal implementation.

If you feel that making your code compatible with other client versions
requires some effort, please either [create patches](https://git-scm.com/docs/git-format-patch)
for each other Minecraft client versions if you know how to port your code, or
discuss it with us when you open a pull request.

For more information about testing your code's compatibility, please refer to
[this section](#testing-for-multiple-client-versions).

[Return to top](#table-of-contents)

## Patch Instructions

### Description of Branches

HBW Helper's repository has several branches. It is important to know each
branch's purpose and which branch you should submit pull request to.

- **Release branch for latest clients**

  The branch whose name ends in `latest` contains source code of this mod's
  latest release for the latest Minecraft client version(s). Only when a commit
  is examined to be stable enough would it be merged to this branch. Therefore,
  you **should not** submit your pull request to this branch.

- **Development branch for latest clients**

  The `dev` branch's code is based on the branch mentioned above, and
  additionally, it contains most recent changes that may be merged to the
  branch above during the next update of this mod. This is **the correct base
  branch** of most of your pull request.

- **Release branch for older clients**

  There are some other branches whose name consists of one or two Minecraft
  version tags (`1.8.9`, `1.9-1.10.2`, etc.). Each of them contains source code
  of this mod's latest release for older Minecraft version(s) suggested by the
  branch name. Unless your commit deals with a version-specific problem, you
  should not submit your pull request to this branch.

- **Experimental topic branch**

  Any other branches are experimental branches which introduce a complicated
  new feature, resolve a tricky bug, or make any other significant change. When
  these branches become more stable, they will be merged into the `dev` branch.
  Feel free to contribute to the project held by these branches! However,
  please make sure your commit is related to the project's topic.

### Testing for Multiple Client Versions

As mentioned before, we try to let HBW Helper support as many Minecraft client
versions as possible. Before you submit your change to us, we would like you to
test your code on all Minecraft client versions that HBW Helper currently
supports.

You need to reconfigure your workspace so that it supports multiple Minecraft
Forge versions, then you are ready to test your code across different versions.

#### Reconfiguring Your Project

Please repeat the following instructions for every *release branch for older
clients*.

1. Close any program that is using your project files, e.g. your IDE.

2. [Check out](https://git-scm.com/docs/git-checkout) a *release branch for
   older clients*.

3. Perform the steps in the *[Configuring Your
   Project](#configuring-your-project)* section.

**Note:** you might need to do these steps again on any new release branch we
add in the future, including a new *Release branch for latest clients* that
replaces an older one.

#### Applying Your Change onto Another Branch

1. [Check out](https://git-scm.com/docs/git-checkout) a *release branch for
   older clients*.

2. [Cherry-pick](https://git-scm.com/docs/git-cherry-pick) your commit(s) from
   the branch where you made change.

3. Resolve any conflict occurred.

Now you can test whether your code works in other Minecraft client versions.

[Return to top](#table-of-contents)

## Build Instructions

Although you can start a Minecraft Forge client from your IDE and test your
code quickly, you might want to build a JAR artifact from your code for 
everyday testing.

In fact, we really encourage you to do this because the client you start from 
IDE can have different behaviors from the standalone Minecraft client you 
installed as a program. For example, any code that processes strings with the
section sign (`§`) does not work on the standalone client, though it works on 
the client opened from IDE.

Bring up a shell from a terminal; if you are on Windows, you can open up a
Command Prompt window instead. Navigate to the folder where you have cloned the
repository.

Execute the following command from your shell:
```
./gradlew build
```

Or, in Command Prompt, execute:
```
gradlew build
```

After the command completes, the JAR artifact can be found under the
`build/libs` folder, and you can install it onto the standalone Minecraft
client with Minecraft Forge.

[Return to top](#table-of-contents)

## Ticket Submitting Guidelines

A *ticket* refers to a pull request or an issue. We would like you to follow
these guidelines when you submit a ticket, as they help us understand your IDEA
and allow us to make this mod better.

### Pull Request Guidelines

- Feel free to open up a pull request in any phase in your development! You can
  send a request after you have finished all the work. However, if you have a
  mature idea but have not written very much code for the idea yet, you can
  still send a request to us, sharing what you have got so far and starting
  discussion and collaboration on your idea.

- Before submitting your pull request, please [rebase your
  commits](https://git-scm.com/book/en/v2/Git-Branching-Rebasing) onto your
  pull request's target branch in this repository to merge any changes we made
  since you forked this repository. This helps eliminate potential conflict
  between your code and ours. A rebase is preferred to a merge because the
  former helps to keep a linear commit history that can be easily reviewed and
  bisected through.

- Please include a descriptive title that clearly indicates what your pull
  request does. Hint: think about what you would write in the commit message if
  you are squashing all commits in your pull request into a single commit.

- If your pull request fixes a documented issue, please **do not** include the
  issue's number in the title, since GitHub does not detect the issue number
  you mention in the title. However, **do** include it in your pull request's
  description.

### Issue Guidelines

- Only when you see an **unexpected behavior** should you submit an issue.

  These are examples of unexpected behavior:
  - Crash **due to this mod**
  - Feature stopped working completely
  - Wrong output

- Please **do not** submit any issue in the following categories:
  - Request in any kind, e.g. proposal of new feature, request to upgrade this
    mod for a new Minecraft version
  - Questions, e.g. asking for assistance on installing this mod, asking about
    a programming concept

- Please try to be as **specific** as possible.

  For instance, if you say "client crashes before it starts", at what specific
  time does it crash? Does client quits so instant that you don't even see the
  initialization screen, or does it crash when Forge completes initialization
  and you are about to see the main menu?

  Another example: when you say "client crashes upon use of `/lobby` command",
  are you doing that when you are in a Bed Wars game, or when you are already
  in a lobby?

- For title, please write a concise but also clear description of the issue you
  are reporting. For example, instead of saying "Crash!", please say "Crash
  after Forge completes initialization".

- **Do not** submit duplicate issue. If you see an opened issue about the
  problem you are reporting, please do not submit another issue for the
  problem. However, you are welcome to provide more details in that existing
  issue by making a comment in the ticket!

[Return to top](#table-of-contents)
