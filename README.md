# Hypixel Bed Wars Helper

Hypixel Bed Wars Helper (HBW Helper) is a Minecraft Forge mod which shows you
information you care about in a Hypixel Bed Wars game at a glance.  

## Features
- Detects if you are playing a Hypixel Bed Wars game

## Planned Features
These features may be added in the next update.  
- [x] Shows what armor you wear
- [ ] Shows status effects you get and their remaining times
- [ ] Shows time until the next diamond/emerald generation
- [ ] Shows what team upgrades (the upgrades to be purchased with diamonds) you
have got
- [ ] Displays graphical icons alongside those elements displayed

## Installation
Go to the [Releases](https://github.com/Leo3418/HBWHelper/releases) page, 
download a JAR file for the Minecraft version you are using, and copy it into 
the `mods` folder under your Minecraft game directory.  
**Note**: Make sure you are not copying a JAR whose file name contains `sources` 
into the `mods` folder. These files contain **source code** of this program and 
will not be loaded by Minecraft Forge.

### Deciphering Version Number
HBW Helper's version number tries to stick with
[Minecraft Forge's suggested versioning
scheme](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).
The version number's format is `MCVERSION-MODVERSION`.
- `MCVERSION` indicates the Minecraft client version(s) that this JAR file
supports.  
Sometimes, one JAR file supports multiple Minecraft client versions. In this
case, this field's format will be `[OLDESTVERSION, LATESTVERSION]`. The client
versions supported are from `OLDESTVERSION` to `LATESTVERSION` (inclusive). For
example, `[1.11, 1.12.2]` means this JAR file supports 1.11, 1.12.2, and all
other versions between them.
- `MODVERSION` indicates this mod's version. Two JAR files with the same mod
version number means they have the same or similar features but are tailored
for different Minecraft client versions.

## Contributing
If you want to contribute to this project, you are welcome!  
Please find more information about contributing, including workspace setup
instructions and contribution guidelines, in [`CONTRIBUTING.md`](CONTRIBUTING.md).

## License
HBW Helper is free software licensed under version 3 of the GNU General Public
License (GNU GPLv3).  
By using, modifying, and/or redistributing this software, you agree to the terms
in GNU GPLv3.  
You can read the full text of the license in [`LICENSE.txt`](LICENSE.txt).

## Obtaining Source Code of This Mod
You can download source code of this mod as how you would when you download 
another repository's code on GitHub, but chances are you are interested in 
source code of a particular version, but the branch holding that version's 
source code has been deleted.  
In accordance with GNU GPLv3, we are pleased to provide you source code of 
every version of this mod. However, GitHub can only automatically add source 
code archive of one branch to a release. Therefore, we provide an alternative 
way of downloading source code of every version.  
On the [Releases](https://github.com/Leo3418/HBWHelper/releases) page, you will
find files containing `sources` in their file name. Although they are JAR files,
they contain source code of the other JAR whose file name is identical except 
the `sources` flag.  
To extract the source code, please refer to 
[this guide](https://docs.oracle.com/javase/tutorial/deployment/jar/unpack.html)
from Oracle.  
**For contributors**: the best way for you to download a copy of this mod's 
source code and work on it is to fork this repository and clone your fork to 
your computer, as described in [`CONTRIBUTING.md`](CONTRIBUTING.md).
 