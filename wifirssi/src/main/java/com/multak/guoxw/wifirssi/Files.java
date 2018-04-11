package com.multak.guoxw.wifirssi;

public class Files {

    /***
     *
     * * * * * * * * * * * * * * SELECT 语句* * * * *
     *
     * 1:  distinct  关键字是查询字段不同的结果集 . select distinct  LanType,SongName  from TableSong
     *    distinct 作用于后面跟接的字段，并非只作用于第一个字段。
     *    eg: 当想要查询LanType 不同的结果集，同时需要Songname 字段时怎么写？
     *
     * 2: limit   select distinct  LanType,SongName  from TableSong limit 5 返回结果集前5行
     *
     * 3: limit - offset :  select  * from TableSong limit 4 offset 5   limit4: 输出4行结果  offset:(从第6行处开始输出)
     *
     *
     * 4: order by : 必须保证order by 是select 语句的最后一个子语句(否则报错)
     *
     *  order by 可以跟接多字段排序
     *  desc (倒叙排列)
     *  asc (升序排列 默认效果)
     *  desc，asc 只作用于位于他前面的列,如果想作用于多列可写
     *  select  * from TableSong order  by SongName desc ,SongIndex desc
     *
     * 5: where
     * SQL 过滤 应当在服务端进行过滤，在应用端过滤，需要服务端发送所有数据，导致网络宽带浪费
     * = :等于
     * <> : 不等于
     * != : 不等于
     * < :小于
     * <= :小于等于
     * > : 大于
     * >= : 大于等于
     * !> : 不大于
     * between  and ：指定两个值之间（包含这俩值）
     * is null： 为null 值
     *
     * where songName= ‘songname’  需要单引号包裹
     *
     * 6: And 操作符  给where 语句附近条件
     *  eg: select  * from TableSong  where SongIndex between 10005 and 10010 and SubFileType=4
     *
     *  Or 操作符: 满足其一条件即可
     *
     *  eg:  查询SongIndex >10010的 Lantype =4 或Lantype=20 的数据
     *  select  * from TableSong  where LanType=4 or LanType=20  and SongIndex >10010
     *  上面该语句查询的结果是 select  * from TableSong  where LanType=4 or ( LanType=20  and SongIndex >10010 ）
     * 原因是: and 在求集 过程中优先级 高
     * select  * from TableSong  where (LanType=4 or LanType=20)  and SongIndex >10010
     *
     *
     * 7: in 操作符合
     *  eg: select  * from TableSong  where LanType in(3,20)
     *  在有很多合法选项时，IN操作符的语法更清楚，更直观。
     *  在与其他AND和OR操作符组合使用IN时，求值顺序更容易管理。
     *  IN操作符一般比一组OR操作符执行得更快
     *  IN的最大优点是可以包含其他SELECT语句，能够更动态地建立WHERE子句。
     *  eg: select  * from TableSong  where LanType in(select distinct  LanType from TableSongSinger )
     *
     *  8: not
     *   eg: select  * from TableSong  where  NOT LanType=4
     *   not用来否定(排除)后面的判断条件
     *   eg:  not  in
     *   select  * from TableSong  where LanType  not in(select distinct  LanType from TableSongSinger )
     *
     * 9:  like 通配符
     *   eg: select  * from TableSong  where SongName like '%a%'
     *
     *   % : 在搜索串中，%表示任何字符出现任意次数
     *   _(下划线):  在搜索串中， _ 表示任何字符出现一次
     *
     *  [] : 方括号（[]）通配符用来指定一个字符集，它必须匹配指定位置（通配符的位置）的一个字符
     *  eg: where cust_contact like '[jm]%'  cust_contact 以 j或m开头
     *
     *  [^ ] 用来否定:
     *
     *  10: 字段拼接
     *
     *  Access和SQL Server使用 + 号
     *  DB2、Oracle、PostgreSQL、SQLite和OpenOffice Base使用||
     *  eg:  select  SongName||SongIndex  as Address from TableSong   (将SongName,SongIndex拼接在一起，组成新的字段Address)
     *  TRIM 函数:
     *  大多数DBMS都支持RTRIM()（正如刚才所见，它去掉字符串右边的空格）、LTRIM()（去掉字符串左边的空格）以及TRIM()（去掉字符串左右两边的空格）。
     * select  SongName||'( ' || trim(SongIndex) || ' )' as address from TableSong
     *
     *  11:  操作符 + - * /
     *
     *   eg:  select  SubFileType,LanType ,SubFileType*LanType as num from TableSong
     *
     *  12: 函数使用：
     *   upper() : 转换值为大写  eg:  select  upper(SongName) from TableSong
     *  left() : （或使用子字符串函数） 返回字符串左边的字符
     *  LENGTH(): （也使用DATALENGTH()或LEN()） 返回字符串的长度
     *  LOWER():（Access使用LCASE()） 将字符串转换为小写
     *  LTRIM() :去掉字符串左边的空格
     *  RIGHT(): （或使用子字符串函数） 返回字符串右边的字符
     *  RTRIM() 去掉字符串右边的空格
     *  SOUNDEX() 返回字符串的SOUNDEX值
     *  UPPER()（Access使用UCASE()） 将字符串转换为大写
     *
     *
     * 13：
     * ABS() 返回一个数的绝对值
     * COS() 返回一个角度的余弦
     * EXP() 返回一个数的指数值
     * PI() 返回圆周率
     * SIN() 返回一个角度的正弦
     * SQRT() 返回一个数的平方根
     * TAN() 返回一个角度的正切
     *
     *
     *
     *  14:  聚集函数
     *
     *   avg(LanType):  返回某一列的平均值
     *   count() : 返回行数
     *   select  count(*)  from TableSong   where SongIndex>11000
     *   max(): 返回某一列的最大值，需要指定列名
     *   min():
     *   sum():返回指定列的总和
     *
     *   15: group by
     *     分组
     *   eg： select count(*) as num, LanType  from TableSong group by LanType
     *
     *    having(): 用来过滤分组
     *    eg: select count(*) as num, LanType  from TableSong group by LanType  having count(*)>4
     *
     *
     *
     *   16: 联结
     *
     *  自联结：
     *   eg: select  c1.SongIndex, c1.LanType,c1.SongName
     *      from  TableSong as c1,TableSong as c2
     *      where (c1.LanType=c2.LanType)  and c2.[SongName]='Okean'
     *
     *      17: 组合查询
     *           UNION必须由两条或两条以上的SELECT语句组成，语句之间用关键字UNION分隔
     *          UNION中的每个查询必须包含相同的列、表达式或聚集函数（不过，各个列不需要以相同的次序列出）。
     *          列数据类型必须兼容：类型不必完全相同，但必须是DBMS可以隐含转换的类型、
     *
     *
     * * * * * * * * * * * * * * * Insert 语句 * * * * * * *
     *
     *  1: 插入完整的行数据
     *   eg:  insert into HotSong values('2','2','2')
     *   该insert 语句很简单，但是数据插入依赖表中字段的次序。
     *
     *   insert into HotSong (SongIndex,Score,Hots) values('4','6','8')
     *   将表中字段明确给出，values和其对应即可。
     *
     *
     *  insert into HotSong (SongIndex,Score,Hots) values('4','6','8')
     *  eg:  如果列定义为允许NULL值（无值或空值）
     *      在表定义中给出默认值。这表示如果不给出值，将使用默认值可以省略行
     *
     *   2: insert select
     *      INSERT一般用来给表插入具有指定列值的行。
     *      INSERT还存在另一种形式，可以利用它将SELECT语句的结果插入表中，
     *      这就是所谓的INSERT SELECT。顾名思义，它是由一条INSERT语句和一条SELECT语句组成的。
     *      eg: insert into TableSong(LanType) select  LanType from TableSong where SongIndex=1558
     *
     * 3:delete
     * delete from tableName where
     *  如果想从表中删除所有行，不要使用DELETE。可使用TRUNCATE TABLE语句，它完成相同的工作，而速度更快
     *
     *
     * *************** create ******
     *
     *      create Table  testTable (
     *      name char(10)  not null,
     *      sex char(10)  not null,
     *      age char(10)  not null
     *      );
     *
     * create Table if not exists  testTable2 (
     * id integer primary key autoincrement,
     * name char(10)  not null,
     * sex char(10)  not null,
     * age char(10)  not null
     * );
     *
     *  3: 删除表(不是删除表数据)： DROP TABLE testTable;
     *
     *  4: 创建view
     *  create view TestTable1 as
     *  select TableSinger.[SingerIndex],TableSong.[SingerName],TableSong.SongName
     *  from TableSong left join  TableSinger
     *  on TableSinger.[SingerName]=TableSong.SingerName
     *
     *
     *

     */


}
