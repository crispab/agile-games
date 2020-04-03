# Learning

The driving factor behind this was to learn new things, the game
is more of a catalystor.

I want to learn several things

## Specificaition by Example

Although I have previous experiences with BDD tools such as Cucumber and Spock, 
I feel slightly rustyy. But the main learning I am pursuing iks the
specification part rather than the testing.

The tools are  working well as test tools, I am intereted in writing accessible and
readable specifications. That is part tool and part language.

So far I have tried it on my wife and got some valuable feedback. IKt is
especially hard to convey something that is grhcial, such as a game
board. I ended up in some kind of ascii art.

What is about specification vs test? A specivfication describes 
functions in the system. It will only be what's interesting from a
business rules perspective. So everything in it must be fulfilled 
by the system but it is only a subset of it. 

For exsmple, the specification talks about players having names but 
it does not state how these names are given. That may be left open
or be described later versions of the specification. It does not talk 
about what users see on screen. That is design bur we are not testing
here and user experience is nnot achioeved by writing something in
Gherkin.

While working with Cucumber, I learned that there are reporting 
tools but the output from them are the result of running a suite.
That is a mindset of testing and I was more interested in specification
as said. 

Being readabie is a main concern so I rtied printing from 
the development environment, copy and paste into Word but qwas not 
happy. I ended up writing an extremly simple Gherkin parser that 
outouts HTML which Word can easily digest. Takes all the specification
files in a directory tree and put everything in one HTML file.
When you open the HTML file in Word, you can table of contents and
change the formattting.

## Web sockets 

I have done designs with REST API that are used by an Elm client.
I would like to learn designing ssomething built on web ssockets
and event queues.

## Micronaut 

I have bbuilt several services on Spring Boot but there is a new 
contender, Micronaut and I think that it would be fun to learn 
what it is like to build a complete system with it.

## Gradle

I am used to Maven so trying out Gradle I think is a fun challenge. 
Exspecially since I would like to build an Elm client as part of
the build.

## Elm and Bootstrap

After used elm-ui which is a very Elm specific library, I felt 
like trying Bootstrap with an Elm clioent. I have not used
Bootstrap for years so I have forgot a lot and there are many 
new things. Mostly it is built on Flexbox which was not a thing
back then. There is an Elm library which makes it type safe
so I will go with that. 