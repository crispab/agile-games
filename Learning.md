# Learning

The driving factor behind this was to learn new things, the game
is more of a catalyst.

I want to learn several things

## Specification by Example

Although I have previous experiences with BDD tools such as Cucumber and Spock, 
I feel slightly rusty. But the main learning I am pursuing is the
specification part rather than the testing.

The tools are  working well as test tools, I am interested in writing accessible and
readable specifications. That is part tool and part language.

So far I have tried my first attempts on writing in Gherkin with my wife and got valuable 
feedback. It is especially hard to convey something that is graphical, such as a game
board. I ended up in some kind of ascii art.

What is it about specification vs test? A specification describes 
functions in the system. It will only be what's interesting from a
business rules perspective. So everything in it must be fulfilled 
by the system but it is only a subset of it. 

For example, the specification talks about players having names but 
it does not state how these names are given. That may be left open
or be described later versions of the specification. It does not talk 
about what users see on screen. That is design but we are not testing
here and user experience is not achieved by writing something in
Gherkin.

While working with Cucumber, I learned that there are reporting 
tools but the output from them are the result of running a suite.
That is a mindset of testing and I was more interested in specification
as said. 

Being readable is a main concern so I first tried printing from 
the development environment, then copy and paste into Word but was not 
happy. I ended up writing an extremely simple Gherkin parser that 
outputs HTML which Word can easily digest. Takes all the specification
files in a directory tree and put everything in one HTML file.
When you open the HTML file in Word, you can table of contents and
change the formatting.

## Web sockets 

I have done designs with REST API that are used by an Elm client.
I would like to learn designing something built on web sockets
and event queues.

## Micronaut 

I have built several services on Spring Boot but there is a new 
contender, Micronaut and I think that it would be fun to learn 
what it is like to build a complete system with it.

## Gradle

I am used to Maven so trying out Gradle I think is a fun challenge. 
Especially since I would like to build an Elm client as part of
the build.

## Elm and Bootstrap

After used `elm-ui` which is a very Elm specific library, I felt 
like trying Bootstrap with an Elm client. I have not used
Bootstrap for years so I have forgot a lot and there are many 
new things. Mostly it is built on Flexbox which was not a thing
back then. There is an Elm library which makes it type safe
so I will go with that. 