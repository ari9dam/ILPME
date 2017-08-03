initiatedAt(location(V1,V2),V3):-happensAt(move(V1,V2),V3),arg1(V1),arg2(V2),time(V3).
initiatedAt(location(V1,V2),V3):-happensAt(journey(V1,V2),V3),arg1(V1),arg2(V2),time(V3).
terminatedAt(location(V1,V2),V3):-happensAt(travel(V1,V4),V3),arg1(V1),arg2(V2),time(V3),arg2(V4).
initiatedAt(location(V1,V2),V3):-happensAt(travel(V1,V2),V3),arg1(V1),arg2(V2),time(V3).
initiatedAt(location(V1,V2),V3):-happensAt(go_back(V1,V2),V3),arg1(V1),arg2(V2),time(V3).
initiatedAt(location(V1,V2),V3):-happensAt(go(V1,V2),V3),arg1(V1),arg2(V2),time(V3).
%story
 happensAt(go_back(mary,office),1).
happensAt(go_back(john,bathroom),2).
happensAt(go(john,hallway),4).
happensAt(journey(mary,hallway),5).
happensAt(travel(sandra,hallway),7).
happensAt(travel(mary,office),8).
happensAt(go_back(mary,hallway),10).
happensAt(travel(sandra,office),11).
happensAt(go(john,kitchen),13).
happensAt(go(sandra,hallway),14).
%examples
:-not holdsAt(location(mary,office),3).
:-not holdsAt(location(mary,hallway),6).
:-holdsAt(location(mary,office),6).
:-not holdsAt(location(john,hallway),9).
:-not holdsAt(location(sandra,office),12).
:-not holdsAt(location(mary,hallway),15).
:-holdsAt(location(mary,office),15).

fluent(location(X,Y)):-arg1(X),arg2(Y).
time(1..16).
holdsAt(F,T+1):- fluent(F),time(T), initiatedAt(F,T).
holdsAt(F,T+1):- fluent(F),time(T), holdsAt(F,T), not terminatedAt(F,T).

arg2(garden;bathroom;hallway;bedroom;office;kitchen).
arg1(mary;sandra;jason;daniel;john).