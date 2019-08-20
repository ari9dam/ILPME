ILPME
=====

**ILPME** is a nonmonotonic ILP *(Inductive Logic Programming)* system that learns from multiple distinct examples. Please see the paper for the details: https://arxiv.org/pdf/1802.07966.pdf 

Running the code
================
To run either checkout the code from git and run with Eclpise or any Java IDE  or download the JAR file from here: [ilpme.jar](https://drive.google.com/open?id=1rTd6jdnWj2JsA48PS2G4WIsUsd__MmeE)

The executable has the following parameters:
- --clasp   : the path to the clasp executable. Download the required version from here [clingo-3.0.5](https://drive.google.com/open?id=16iCoPvvtP90Fv6eAn9ecOvRMOadSDvVj) 
- --gringo  : the path to the gringo executable. Download the required version from here [clingo-3.0.5](https://drive.google.com/open?id=16iCoPvvtP90Fv6eAn9ecOvRMOadSDvVj) 
- --source  : the path to the directory that contains the training data. The directory should contain the .sample files, normally one for                 each  of training data sample; one or more .bk files containing common knowledge about the task and one or more .m (mode)                   files. You can download one sample source folder from here: [sample-input](https://drive.google.com/open?id=1N8NdWI1vuMCmnAH5nJjilfSf6uqqtiTt) . The .bk files contain normal asp programs. The .sample and .m               files uses #example, #modeb, #modeh directives, the meaning and syntax of which is described here: [xhail readme](https://github.com/stefano-bragaglia/XHAIL)   

The rest of the parameters are optional and provides controls over the search algorithm. Let's say that the program has found a solution **H** for the first n samples and trying to extend the solution to **H'** that also overs the n+1-th sample. The parameters then plays the provides the following flexibility:
- --maxLength   : This sets a limit on the maximum number of literals that you can add to **H**. Basically, (the number of literals in **H'**) - (the number of literals in **H**) <= provided maxLength. The dafault value is infinity.
- --refinement  : This limits the number of **H'** to be found. In general, the search algorith will try to find all possible **H'**. But if --refinement X is specifed then, the search algorithm stops as soon as it finds X number of **H'**. 
--subiterations : the search algorithm which finds  **H'** is an iterative algorithm i.e. at each step it takes a partial solution and updates it untill it could find enough number of **H'**. This option to stop the search if the supplied number of the steps has been executed. In case, both --refinement and --subiterations are specified the search stops as soon as one of the two exit condition is fires.
--width         : the search algorthm is an backtracking algorithm. This option limits the size of pool of candidate partial solutions. When omitted no such bound is enforced.

This parameters can significantly boost the runtime. So, it's better to use some of them, than to avoid it. Here is a sample run command for the [sample-input](https://drive.google.com/open?id=1N8NdWI1vuMCmnAH5nJjilfSf6uqqtiTt):

java -jar ilpme.jar -c C:\Users\Arindam\Downloads\clingo-3.0.5-win64\clasp.exe -g C:\Users\Arindam\Downloads\clingo-3.0.5-win64\gringo.exe --source "C:\Users\Arindam\Desktop\sample-input"  --width 1 --refinement 1

The code on my desktop produces the following output:

```
...
Iteration 10 :
# of Generaisations: 1
#sub iterations 1 for 35.sample, queue 0
 Finding inductions...35.sample
Parsing input
number of models-2
Found 1 inductions.size: 33-> 33-> 35 | 211
#sub iterations : 2, found refinements: 1
#sub iterations : 2, found refinements: 1
create(V1,V2):-eobservedAt("condense","what does something v into ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("come","what v into something ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("turn","what does something v into ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("cause","what v something ?",V1,V2),doesNotexists(V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("melt","what does something v into ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-description(V1,"water"),entityObservation("absorb","what is v into something ?","precipitation",V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("form","what is v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("become","what does something v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("go","what v into something ?",V1,V2),doesNotexists(V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("release","how is something v somewhere ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("go","what v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-entityObservation("erode","what v something ?","the river",V2),description(V1,"valley"),entity(V1),time(V2).
create(V1,V2):-eobservedAt("form","what does something v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("fall","how does something v somewhere ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-entityObservation("push","what v ?","the magma",V2),description(V1,"lava"),entity(V1),time(V2).
best coverage 10
current hypothesis coverage 9
current hypothesis size 211
queue size 2

Found a solution!
Time in minutes: 0.18068333333333333
create(V1,V2):-eobservedAt("condense","what does something v into ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("come","what v into something ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("turn","what does something v into ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("cause","what v something ?",V1,V2),doesNotexists(V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("melt","what does something v into ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-description(V1,"water"),entityObservation("absorb","what is v into something ?","precipitation",V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("form","what is v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("become","what does something v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("go","what v into something ?",V1,V2),doesNotexists(V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("release","how is something v somewhere ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("go","what v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-entityObservation("erode","what v something ?","the river",V2),description(V1,"valley"),entity(V1),time(V2).
create(V1,V2):-eobservedAt("form","what does something v ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-eobservedAt("fall","how does something v somewhere ?",V1,V2),entity(V1),time(V2).
create(V1,V2):-entityObservation("push","what v ?","the magma",V2),description(V1,"lava"),entity(V1),time(V2).
```

Contact
=======
Please create issues on the github page if you have any questions. You can also email me at amitra7@asu.edu.


