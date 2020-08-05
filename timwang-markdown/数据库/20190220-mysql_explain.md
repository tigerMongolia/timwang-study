## **一、何谓索引？**

​             索引，简单说，就是排好序，查找的快的一种数据结构；

## **二、sql为何慢？**

​          **1、查询语句写的烂；**

​          **2、索引失效：（单值、复合）**

​          **3、关联查询太多**

​          **4、服务器调优（缓冲、线程数）**

## **三、索引优势与劣势；**

​            **优势：提高检索效率，降低IO成本；降低数据的排序成本，降低CPU的消耗；**

​            **劣势：索引也是一张表，该表保存了主键与索引字段，并指向实体表的记录，所以索引也需要占用空间，其次，在更新表时，不仅要更新数据，还要更新索引；**

## **四、索引分类？**

​           **单值索引：只包含一个列；**

​           **唯一索引：索引列的值必须时唯一的，不过可以有空值；**

​           **复合索引：一个索引包含多个列；**

## **五、主角——explain**

​           **1、是什么？**

​                  **使用explain可以模拟优化器执行sql语句，从而知道mysql是如何执行sql语句的**

​          **2、能干嘛？**

​                  **表的读取顺序、数据读取操作的操作类型、那些索引可以使用、那些索引被实际使用、表之间的引用、每张表有多少行被优化器查询**

​          **3、怎么用？**

​                  **explain + sql**

​          **4、长什么样？**

​            **![研发部 > Mysql——Explain > WechatIMG4.jpeg](/Applications/Typora.app/Contents/Resources/TypeMark/Docs/img/WechatIMG4.jpeg)**

​          **id: select 查询的编号，表示查询顺序：**

​                 **id相同，顺序执行，由上到下；**

​                 **id不同，如果是自查询，ID的序号会增加，ID值越大，优先级越高，越先被执行**

​          **select_type:有哪些？**

​                 **simple、primary、subquery、derived、union、union result；**

​         **作用？**

​              **simple：简单查询，不包含任何自查询和关联查询；**

​                       **EXPLAIN SELECT \* from PROJECT where id = 1**

​              **primary：查询中包含任何复杂的子部分，最外层查询则被标记为primary**

​                      **EXPLAIN SELECT \* from BUILDING b LEFT join (select building_id,sum(UNOCCUPIED_AREA) as area from   RENTAL_UNIT ru group by building_id having area > 40000 ) r on r.building_id  = b.ID where b.ID > 13231**

​             **subquery：在select或者where字句中包含的复杂子查询**

​                      **EXPLAIN SELECT (select Id from PROJECT ) from DUAL**

​                      **EXPLAIN select \* from DEMAND_SITE_VISIT where CREATE_TIME > (SELECT MAX(CREATE_TIME) from DEMAND_FOLLOW_LOG )**

​            **derived：在from中包含子查询被标记为derived，mysql会递归执行这些子查询，把结果放在临时表里面；**

​                      **EXPLAIN SELECT \* from BUILDING b LEFT join (select building_id,sum(UNOCCUPIED_AREA) as area from   RENTAL_UNIT ru group by building_id having area > 40000 )r on r.building_id  = b.ID**

​                      **where b.ID > 13231**

​           **union：如果第二个select出现在union后，则会被标记为union，如果union包含在from子句的子查询中，则外层的select被标记为derived；**

​                       **explain  SELECT ID FROM DEMAND  UNION select REMARK from DEMAND_FOLLOW_LOG** 

​          **union result：从union结果中获取的select**

​                      **explain  SELECT ID FROM DEMAND  UNION select REMARK from DEMAND_FOLLOW_LOG** 

 

​         **table：显示操作的是哪一张表；**

​         **type：system>const>eq_ref>ref>ref_or_null>index_merge>unique_subquery>index_subquery>range>index>all**

​                 **System:表中只有一条记录，这是const的实例，可以忽略**

​                              **explain select \* from (select id from PROJECT where id = 15) a**

​                 **const：通过索引一次就可以找到了，一般出现在primary key或者唯一索引的地方，所以快得很；**

​                              **explain select \* from PROJECT where id = 15**

​                **eq_ref:简单说，用于链表查询，按照联表的主键或者唯一建删除，读取本表和关联联表中的每行组合成的一行，eq_ref可以使用=匹配索引的列，比较值可以是常量或者使用此表之前读取的表中的列的表达式,当连接使用索 引的所有部分时，索引是主键或唯一非null索引时，将使用该值；**

​                              **explain SELECT \*  FROM PROJECT p,BUILDING b where p.ID = b.PROJECT_ID and p.CITY_CODE = '310100'**

​                 **ref：非唯一性索引扫描，返回某个匹配单独值的所有行，本质上也是一种索引访问，它返回所有匹配某个单独值的行，然而，它可能会找到多个符合条件的行，所以他应该属于查找和扫描的混合体；**

​                              **EXPLAIN SELECT \* from FUNNEL_REPORT where UTM_SOURCE = 'baidu’;**

​                              **explain SELECT \* from PROJECT p LEFT join BUILDING b on p.ID = b.PROJECT_ID** 

​                 **range：只检索给定范围的行，使用一个索引来选择行，key列显示使用了哪个索引，一般就是在where语句中出现了between、<、>、in等查询语句，这种范围扫描 索引比全表扫描要好，因为它只需要开始于索引的某一点，而结束于另一点；**

​                              **explain SELECT \* from PROJECT where  id > 1890**

​                  **Index merge:where子句包含多个条件（and 或者or连接），mysql在5.1开始，开始了index merge，其实就是对多个索引列进行条件扫描，然后将各个结果进行合并；**

​                  **index：full index scan，index与ALL区别为index类型只遍历索引树，这通常比ALL快，因为索引文件通常比数据文件小；**

​                  **all：full table scan，将便利全表以找到匹配的行**

​                **一般来说，要保证查询至少达到range级别，最好能达到ref；**

​               **possible_keys:显示可能应用在这张表中的索引，一个或多个，查询涉及到字段上若存在的索引，则该索引将被列出，但不一定被查询实际用到；**

​               **key：实际使用的索引，如果为null，则没有使用索引，查询中若使用了覆盖索引，则该索引和查询的select字段重叠；**

​                         **explain  SELECT MOBILE_NO,STATUS from CONTACT where NAME = 'f123dfff' and `TYPE`=4** 

​               **key_len：表示索引中使用的字节数，可通过该列计算查询中使用的索引长度，在不损失精确性的情况下，长度越短越好，显示的值为索引字段的最大可能长度，是一个常数，并非实际使用长度，即key_len是根据表定义计算而得，不是通过表内检索出的；（可计算 default null：1字节、varchar：2字节、tinyint：1字节）**

​               **ref：显示索引的哪一列被使用了，如果可能的话，是一个常数，那些列或常量被用于查找索引列上的值；**

​              **rows：根据表统计信息及索引选用的情况，大致估算出找到所需的记录需要读取的行数；**

​              **extra：**

​                        **using filesort：说明mysql会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取，mysql中无法利用索引完成的排序操作称为文件排序；**

​                                 **explain  SELECT MOBILE_NO,STATUS from CONTACT where NAME = 'f123dfff' and type = 4 order by MOBILE_NO**

​                        **using temporary：使用了临时表保存中间结果，mysql在对查询结果排序时使用临时表，常见于排序orderby和分组group by；**

​                                 **explain SELECT  \* from CONTACT group by `TYPE`**

​                        **using index：表示相应的select操作中使用了覆盖索引，避免访问了表的数据行，效率不错，如果同时出现using where，表明索引被用来执行索引键值的查找，如果没有同时出现using where，表明索引用来读取数据而非执行查找动作；**

​                                 **explain  SELECT MOBILE_NO,STATUS from CONTACT where NAME = 'f123dfff' and type = 4 order by MOBILE_NO DESC**

​                        **using where：表明使用了where过滤；**

​                                 **explain  SELECT MOBILE_NO,STATUS from CONTACT where NAME = 'f123dfff' and type = 4 order by MOBILE_NO DESC**

​                        **using join buffer：使用了连接缓存;只有当join类型为all，index，range后者是index_merge的时候才会使用join buffer；**

​                        **impossible where ：where子句的值总是false，不能用来获取仍和元组；**

​                                 **explain  SELECT MOBILE_NO,STATUS from CONTACT where 1=2**

​                        **select table optimized away：在没有groupby子句的情况下，基于索引优化min/max操作或者对于myisam存储引擎优化count(\*)操作，不必等到执行阶段再进行计算，查询计划生成的阶段即完成优化；**

​                        **distinct：优化distinct操作，在找到第一匹配的元组后即停止找同样值的动作；**

 

**索引失效与优化：**

​       **1、全部匹配最佳**

​       **2、最佳最前缀法则（如果索引了多列，要遵守最左前缀法则，指的是查询从索引的最左前列开始并且不跳过索引中的列）**

​       **3、不在索引列上做任何操作（计算、函数、类型转换）**

​       **4、（避免select \*）尽量使用覆盖索引**

​       **5、存储引擎不能使用索引中范围条件右边的列**

​       **6、mysql在使用不等于的时候无法使用索引会导致全表扫描**

​       **7、is null，is not null也无法使用索引**

​       **8、like以通配符开头（“%abc”）mysql索引失效会变成全表扫描的操作**

​       **9、字符串不加单引号索引会失效**

​      **10、少用or，用它来连接时会索引失效**

**一般性的建议：**

​      **对于单值索引，尽量选择针对当前的query过滤性更好的索引；**

​      **在选择组合索引的时候，当前query中过滤性最好的字段在索引字段顺序中，位置越靠前越好；**

​      **在选择组合索引的时候，尽量选择可以能够包含当前query中的where字句中更多字段的索引；**

​      **尽可能通过分析统计信息和调整query的写法来达到选择合适索引的目的；**

**查询截取分析：**

   **查询优化：**

​         **永远小表驱动大表**

​         **order by关键字优化：尽量使用index方式排序，避免使用filesort方式排序；**

​         **尽可能在索引列上完成排序操作，遵照索引建立最佳左前缀；**

​        **如果不在索引列上，filesort有两种算法，mysql就要启动双路排序和单路排序；**

​        **双路排序：mysql4.1之前使用双路排序，字面意思是两次扫描磁盘，最终得到数据，读取行指针和orderby列，对他们进行排序，然后扫描已经排序好的列表，按照列表中的值重新从列表中读取对应的数据输出，从磁盘取排序字段，在buffer进行排序，再从磁盘取其他字段；**

​       **单路排序：从磁盘读取查询需要的所有列，按照orderby列在buffer对他们进行排序，然后扫描排序后的列表进行输出；它的效率更快一些，避免了第二次读取数据，并且把随机IO变成了顺序IO，但是它会使用更多的空间；**

   **优化策略：**

​         **增大sort_buffer_size参数的设置；**

​         **增大max_length_for_sort_data参数的设置；**

 **groupby关键字的优化：**

​          **groupby实质是先排序后进行分组，遵照索引创建的最佳左前缀；**

​          **当无法使用索引列，增大max_length_for_sort_data和sort_buffer_size参数的设置；**

​          **where高于having，能写where限定的条件就不要去having限定了；**

**实例：**

​        1、SELECT COUNT(DISTINCT [a.ID](http://wiki.clinks.com.cn/a.ID)) AS COUNT

​        FROM PROJECT a

​        LEFT JOIN LEADS d ON d.PROJECT_ID = [a.ID](http://wiki.clinks.com.cn/a.ID)

​        LEFT JOIN PROJECT_COMPANY_ASSN pc ON pc.PROJECT_ID = [a.ID](http://wiki.clinks.com.cn/a.ID)

​        LEFT JOIN OWNER c ON c.COMPANY_ID = pc.COMPANY_ID AND pc.STATUS = 1 AND pc.TYPE = 3

​        WHERE d.STATUS != 12 AND a.STATUS in (   0 )  AND d.STATUS =  7

​       2、SELECT

​        COUNT(1) AS VIEW_COUNT,

​        pd.PROJECT_ID

​        FROM PROJECT_DETAIL_VIEW_LOG pd

​        INNER JOIN PROJECT p

​        ON [p.ID](http://wiki.clinks.com.cn/p.ID) = pd.PROJECT_ID

​        WHERE p.PROVINCE_CODE = '320281'

​        OR p.CITY_CODE='320281'

​        OR p.DISTRICT_CODE='320281'

​        GROUP BY pd.PROJECT_ID

​        ORDER BY VIEW_COUNT DESC

​        LIMIT 0,10

​      