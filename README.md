# SoStream

In this project, we present a solution to the DEBS 2016 Grand Challenge that leverages Apache Flink, an open source platform for distributed stream and batch processing. The challenge focuses on the real-time analysis of an evolving social-network graph, to (1) determine the posts that trigger the current most activity, and (2) identify large communities that are currently involved in a topic.

We design the system architecture focusing on the exploitation of parallelism and memory efficiency so to enable an effective processing of high volume data streams on a distributed infrastructure.

Our solution to the first query relies on a distributed and fine-grain approach for updating the post scores and determining partial ranks, which are then merged into a single final rank. Furthermore, changes in the final rank are efficiently identified so to update the output only if needed.

The second query efficiently represents in-memory the evolving social graph and uses a customized Bron-Kerbosch algorithm to identify the largest communities active on a topic. We leverage on an in-memory caching system to keep the largest connected components which have been previously identified by the algorithm, thus saving computational time.

We run the experiments on a single node equipped with 4 cores and 8 GB of RAM. For a medium dataset size with 200k events, our system can process up to 2.5(?) kEvents/s with an average latency of 0.4(?) ms for the first query, and up to 2.8(?) kEvents/s with an average latency of 0.5(?) ms for the second query.


## Grand Challlenge Problem
The ACM DEBS 2016 Grand Challenge is the sixth in a series of challenges which seek to provide a common ground and uniform evaluation criteria for a competition aimed at both research and industrial event-based systems. The goal of the 2016 DEBS Grand Challenge competition is to evaluate event-based systems for real-time analytics over high volume data streams in the context of graph models.

The underlying scenario addresses the analysis metrics for an evolving social-network graph. Specifically, the 2016 Grand Challenge targets the following problems: (1) identification of the posts that currently trigger the most activity, and (2) identification of large communities that are currently involved in a topic. The corresponding queries require continuous analysis of an evolving graph structure under the consideration of multiple streams that reflect updates to the graph.

The data for the DEBS 2016 Grand Challenge is based on the dataset provided together with the [LDBC](http://ldbcouncil.org/) Social Network Benchmark. It takes up the general scenario from the 2014 SIGMOD Programming contest but - in contrasts to the SIGMOD contest - explicitly focuses on processing streaming data. Details about the data, the queries for the Grand Challenge, and information about how the challenge will be evaluated are provided below.


### Input Data Streams
The input data is organized in four separate streams, each provided as a text file. Namely, we provide the following input data files:

* **fiendship.dat**: (ts, user\_id\_1, user\_id\_2), where *ts* is the friendship's establishment timestamp, *user_id_1* is the id of one of the users, *user_id_2* is the id of the other user.

* **posts.dat**: (ts, post\_id, user\_id, post, user), where *ts* is the post's timestamp, *post_id* is the unique id of the post, *user_id* is the unique id of the user, *post* is a string containing the actual post content, *user* is a string containing the actual user name.

* **comments.dat**: (ts, comment\_id, user\_id, comment, user, comment\_replied, post\_commented), where *ts* is the comment's timestamp, *comment_id* is the unique id of the comment, *user_id* is the unique id of the user, *comment* is a string containing the actual comment, *user* is a string containing the actual user name, *comment_replied* is the id of the comment being replied to (-1 if the tuple is a reply to a post), *post_commented* is the id of the post being commented (-1 if the tuple is a reply to a comment).

* **likes.dat**: (ts, user\_id, comment\_id), where *ts* is the like's timestamp, *user_id* is the id of the user liking the comment, *comment_id* is the id of the comment.

Each file is sorted based on its respective timestamp field.

A sample set of input data streams can be downloaded from [here](https://www.dropbox.com/s/vo83ohrgcgfqq27/data.tar.bz2).


### Query 1
The goal of query 1 is to compute the top-3 scoring active posts, producing an updated result every time they change.

The total score of an active post P is computed as the sum of its own score plus the score of all its related comments. Active posts having the same total score should be ranked based on their timestamps (in descending order), and if their timestamps are also the same, they should be ranked based on the timestamps of their last received related comments (in descending order). A comment C is related to a post P if it is a direct reply to P or if the chain of C's preceding messages links back to P.

Each new post has an initial own score of 10 which decreases by 1 each time another 24 hours elapse since the post's creation. Each new comment's score is also initially set to 10 and decreases by 1 in the same way (every 24 hours since the comment's creation). Both post and comment scores are non-negative numbers, that is, they cannot drop below zero. A post is considered no longer active (that is, no longer part of the present and future analysis) as soon as its total score reaches zero, even if it receives additional comments in the future.


#### Output
The output should comply the following structure:

> (ts,top1\_post\_id,top1\_post\_user,top1\_post\_score,top1\_post\_commenters,
top2\_post\_id,top2\_post\_user,top2\_post\_score,top2\_post\_commenters,
top3\_post\_id,top3\_post\_user,top3\_post\_score,top3\_post\_commenters)

where the fields are:

* **ts**: the timestamp of the tuple that triggers a change in the top-3 scoring active posts appearing in the rest of the tuple.
* **topX_post_id**: the unique id of the top-X post.
* **topX_post_user**: the user author of top-X post.
* **topX_post_commenters**: the number of commenters (excluding the post author) for the top-X post.

Results should be sorted by their timestamp field. The character "-"  should be used for each of the fields of any of the top-3 positions that has not been defined. Needless to say, the logical time of the query advances based on the timestamps of the input tuples, not the system clock.

Sample output tuple for the query:

> 2010-09-19 12:33:01,25769805561,Karl Fischer,115,10,25769805933,Chong Liu,83,4,-,-,-,-

> 2010-10-09 21:55:24,34359739095,Karl Fischer,58,7,34359740594,Paul Becker,40,2,34359740220,Chong Zhang,10,0


### Query 2
This query addresses the change of interests with large communities. It represents a version of query type 2 from the 2014 SIGMOD Programming contest. Unlike in the SIGMOD problem, the version for the DEBS Grand Challenge focuses on the dynamic change of query results over time, i.e., calls for a continuous evaluation of the results.

Goal of the query:
Given an integer k and a duration d (in seconds), find the k comments with the largest range, where the range of a comment is defined as the size of the largest connected component in the graph induced by persons who (a) have liked that comment (see likes, comments), (b) where the comment was created not more than d seconds ago, and (c) know each other (see friendships).

Parameters: k, d

Input Streams: likes, friendships, comments

Output:
The output includes a single timestamp ts and exactly k strings per line. The timestamp and the strings should be separated by commas. The k strings represent comments, ordered by range from largest to smallest, with ties broken by lexicographical ordering (ascending). The k strings and the corresponding timestamp must be printed only when some input triggers a change of the output, as defined above. If less than k strings can be determined, the character “-” (a minus sign without the quotations) should be printed in place of each missing string.

The field ts corresponds to the timestamp of the input data item that triggered an output update. For instance, a new friendship relation may change the size of a community with a shared interest and hence may change the k strings. The timestamp of the event denoting the added friendship relation is then the timestamp ts for that line's output. Also, the output must be updated when the results change due to the progress of time, e.g., when a comment is older that d. Specifically, if the update is triggered by an event leaving a time window at t2 (i.e., t2 = timestamp of the event + window size), the timestamp for the update is t2. As in Query 1, it is needless to say that timestamps refer to the logical time of the input data streams, rather than on the system clock.

In summary, the output is specified as follows:

ts: the timestamp of the tuple that triggers a change in the top-3 scoring active posts
comments_1,...,comment_k: top k comments ordered by range, starting with the largest range (comment_1).
Sample output tuples for the query with k=3 could look as follows:

2010-10-28T05:01:31.022+0000,I love strawberries,-,-
2010-10-28T05:01:31.024+0000,I love strawberries,what a day!,-
2010-10-28T05:01:31.027+0000,I love strawberries,what a day!,well done
2010-10-28T05:01:31.032+0000,what a day!,I love strawberries,well done


## Configure a Virtual Machine
The following steps allow to set up a virtual machine in the following environment:
- Oracle Virtual Box (VB): create an Oracle VirtualBox virtual machine with (i) Debian 8 64bit OS (ii) 40GB fixed-size VDI (iii) a user named 'debs' (iv) two processors with PAE/NX and (v) at most 8GB RAM.
- Amazon Web Services (AWS): Create an Amazon AWS virtual machine c3xlarge.

Start the VM.

If you are in VB, click on Devices>Insert Guest Additions CD image.

Download the configuration scripts:

>wget http://gmarciani.com/workspace/sostream/script/{configure-env.sh,configure-app.sh,check.sh}

>chmod +x configure-env.sh configure-app.sh check.sh

Run the *environment configuration* script:

>./configure-env.sh {vbox|aws}

Reboot your system.

Run the *app configuration* script:

>./configure-app.sh {vbox|aws} BRANCH-NAME

where *BRANCH-NAME* is the name of the branch to clone.

Finally run the *check* script:

>./check.sh {vbox|aws}

Notice that all the previous scripts are *idempotent*.

Whenever a new branch release is available, just run the app configuration script followed by the check script.


## Compile the app
The script *app configuration* script automatically compile the official *q1* and *q2* queries. So, if you are in production jump to the next step.

If you want to compile a specific set of queries, run the *compile script*:

>./compile.sh [query,...,query]

where *[query,...,query]* is an optional white-space separated list of queries to compile. Notice that the names of queries must correspond to Maven profile names provided in the project pom.

If you run the script with no arguments:

>./compile.sh

the official queries *q1* and *q2* will be compiled.


## Run the app
Launch the app, running:

>./run.sh FRNDS POSTS CMNTS LIKES K D (CONF)

where *FRNDS* is the friendships data source, *POSTS* is the posts data source, *CMNTS* is the comments data source, *LIKES* is the likes data source, *K* is the rank size for the 2nd query, *D* is the delay for the 2nd query, and *CONF* is the optional YAML configuration file (i.e. /config/app-config.yaml). Notice that only relative paths are accepted.

For test and debugging, launch the app, running:

>./exec.sh DATA K D (CONF)

where *DATA* is the folder containing the dataset (friendships.dat, posts.dat, comments.dat and likes.dat), *K* is the rank size for the 2nd query, *D* is the delay for the 2nd query, and *CONF* is the optional YAML configuration file (i.e. /config/app-config.yaml).

Note that: the scripts *run* and *exec* is responsible of start/stop Apache Flink; you do not need to start/stop it manually.

## Configure an AWS EC2 instance
Run the *AWS configuration* script

>./aws.sh configure EC2 (PEM-KEY)

where *EC2* is the public DNS of your AWS EC2 instance, and *PEM-KEY* is the .pem file used to establish the SSH connection. By default, the script looks for the key stored in $HOME/.ssh/sostream.pem.

After the script execution, your EC2 instance is ready for code deployment.


## Deploy the code on AWS
Run the *AWS configuration* script

>./aws.sh deploy EC2 (PEM-KEY)

where *EC2* is the public DNS of a configured AWS EC2 instance, and *PEM-KEY* is the .pem file used to establish the SSH connection. By default, the script looks for the key stored in $HOME/.ssh/sostream.pem.

After the script execution the project in AWS is in-sync with your local project. It is suggested to run the *check* script on your EC2 instance, to verify that everything is ok. Now you can run the app as usual with the *run* script.


## Authors
Giacomo Marciani [giacomo.marciani@gmail.com](mailto:giacomo.marciani@gmail.com)

Marco Piu [pyumarco@gmail.com](mailto:pyumarco@gmail.com)

Michele Porretta [micheleporretta@gmail.com](mailto:micheleporretta@gmail.com)

Matteo Nardelli [nardelli@ing.uniroma2.it](mailto:nardelli@ing.uniroma2.it)

Valeria Cardellini [cardellini@ing.uniroma2.it](mailto:cardellini@ing.uniroma2.it)


## Institution
Department of Civil Engineering and Computer Science Engineering, University of Rome Tor Vergata, Italy


## License
The project is released under the [BSD License](https://opensource.org/licenses/BSD-3-Clause).
Please, read the file LICENSE.md for details.
