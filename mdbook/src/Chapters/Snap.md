# SNAP
[![rpncalc](https://snapcraft.io//rpncalc/badge.svg)](https://snapcraft.io/rpncalc)

I would encourage anyone with a supported Linux platform to use the Snap package of RPNCalc.  You can learn more about Snap from the [Snapcraft Homepage](https://snapcraft.io).  

At a high level, a Snap is a fully contained container with your application and it's dependencies bundled in.  It runs in a `sandbox` so it can't interfere with other parts of your system until it's given access.  Snaps are automatically kept up to date and are easily removed leaving no trace on the system.  There is even a nice [App Store](https://snapcraft.io/store) where you can find software. 

As an example, RPNCalc uses Java, but you don't have to have Java installed on your machine as it's bundled in with the Snap.

[![Get it from the Snap Store](https://snapcraft.io/static/images/badges/en/snap-store-black.svg)](https://snapcraft.io/rpncalc)

As a shameless plug, you can [Search for 'Fross'](https://snapcraft.io/search?q=fross) to see all of my Snap packages.

While I'm a fan of Snaps, there are positives and negatives to using them.

## Snap: The Good and the Bad

***<p align="center">-- Thanks to the good folks at MakeUseOf.com for this! --</p>***

[MakeUseOf.com](https://www.makeuseof.com/everything-you-need-to-know-about-snap-and-snap-store/) has a good breakdown of everything that's good and bad about Snap.  

Ever since Canonical announced Snap, there's been a stir in the Linux community about whether Snap is the right approach to improve package distribution on Linux. This has given rise to two opposing camps: one in favor of Snap and the other critical of its approach in the long run.

Here's a breakdown of everything that's good and bad about Snap.

### Advantages of Using Snap

- Snaps come bundled with dependencies (libraries) that facilitate instant access to a program, as you no longer have to manually install the missing dependencies to make it work on your system.
- Each snap runs in its own containerized sandbox to avoid interference with other system packages. As a result, when you remove a snap, the system removes all of its data, including dependencies, without affecting other packages. Needless to say, this also offers a more secure environment since one package can't access the information of another.
- Snap updates snaps automatically at set intervals. Hence, you always run the latest version of a program on your system.
- Snap makes it easier for developers to distribute their software directly to users, so they don't have to wait for their Linux distribution to roll them out.
- Adding to the previous point, another advantage of putting developers in charge of packaging and distributing their software is that they don't have to create distro-specific packages, as it comes bundled with the required dependencies.

### Disadvantages of Snap

- Since snaps come bundled with dependencies, they're larger in size and occupy more disk space than their counterparts from other package managers.
- As a result of the bundled dependencies, snaps are distributed as compressed filesystem images and you need to mount them first before installing. Because of this, snaps are slower to run than traditional packages.
- Although Snap enables developers to distribute their snaps directly to users, the distribution pipeline requires them to set up an account with Canonical and host their snaps on it. This goes against the true nature of the open-source methodology because even though the software is still open source, the package management system is controlled by an entity.
- Another downside to allowing developers to distribute packages is that the packages don't go through stringent checks and reviews by the community and therefore carry the risk of containing malware---as seen a few years back.
- Due to the fact that Snap's back-end is still closed-source and controlled by Canonical, many major Linux distros aren't on board with the idea of putting Snap as the default package manager on their system.