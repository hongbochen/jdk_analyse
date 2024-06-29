从今天开始，将对java进行深入的学习和理解，主要的学习计划为：
 - 学习java的基本类及其使用方式，同时研究其实现的逻辑，分析其实现的代码
 - java基本知识学习完成之后，学习jvm的原理及实现，掌握java的内存模型，GC原理等。
 - 学习Spring boot中的相关容器，中间件等的实现。
 
现在我将从java的集合开始学起，集合中首先学习ArrayList的实现及其代码分析。

我们通过查看Java 1.8的ArrayList的源码，发现ArrayList继承自AbstractList，并实现了List<E>, RandomAccess, Cloneable, java.io.Serializable这几个接口，我们首先看AbstractList的实现。

而AbstractList继承自AbstractCollection,抽象集合类，抽象集合类又继承自Collection，同样，Collection又继承自Iterable，这里的Iterable是一个接口类，我们看一下这个接口类的源码。

```

package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * 实现这个接口允许对象成为"for-each loop"语句的目标。
 * 
 */
public interface Iterable<T> {
    /**
     * 返回元素类型为T的迭代器
     * @return an Iterator.
     */
    Iterator<T> iterator();

    /**
     * 为迭代器中的每一个元素执行给定的方法，知道所有的元素都被处理或方法抛出一个异常。
     * 除非被实现类指定，方法将会按照迭代器的顺序被执行（如果迭代器顺序被指定的话）。
     * 被方法抛出的异常将会被传递给调用者。
     *
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * 创建一个关于被这个Iterable描述的元素的Spliterator
     * 默认的实现从itarable的Iterator中创建一个早期绑定的spliterator。这个spliterator继承了
     * iterable的迭代器的fail-fast属性。
     * 
     * 默认的实现通常情况下应该被重载。默认实现返回的分离器缺少拆分能力，没有尺寸，并且不会报告
     * 任何分离器特征。实现类可以近乎可能的提供一个更好的实现。
     *
     */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}

```

 ``` 
 package java.util;
 
 import java.util.function.Predicate;
 import java.util.stream.Stream;
 import java.util.stream.StreamSupport;
 
 /**
  * 该接口是集合体系的根接口。一个集合代表一组对象，也被称为集合的元素。
  * 一些结合允许相同的元素，一些集合不允许。一些集合是排好顺序的，一些集合
  * 是没有顺序的。JDK不提供这个接口的任何的直接实现：他只提供一些特定子接口例如
  * Set和List的实现。这个接口通常用于传递集合，并在需要最大通用性的地方对其进行操作。
  *
  * Bags或multisets（可能包含相同元素的非有序集合）应该直接实现该接口。
  *
  * 一个通用目的的Collection实现类（通常通过其子接口之一间接实现集合）应该提供两
  * 个标准的构造器：一个void(没有元素)的构造器，该构造器用于创建空集合；一个类型为
  * Collection的单一参数的构造器，用于创建有相同元素作为他的参数的新的集合。实际上，
  * 后面一种构造器允许用户复制任何容器，产生一个期望实现类型的相等的集合。不可能去强制
  * 这个惯例（因为接口不可能包含构造器）但是所有在Java平台库中的通用目的Collection实现
  * 都遵循了这个惯例。
  *
  * "破坏性"方法包含在这个接口中了，也就是，修改他们操作的集合的方法，如果集合这个不支持操作，
  * 将会抛出UnsupportedOperationException。如果是这种情况，如果调用对集合没有影响，
  * 则这些方法可能（但不是必须）会抛出将会抛出UnsupportedOperationException。
  * 例如，在一个不可修改的集合中调用addAll(Collection)方法（但不是必须），如果被添加的
  * 集合是空的，则可能会抛出异常。
  *
  * 一些集合实现在他们包含的元素会有一些限制。例如，一些实现禁止空元素，并且一些实现
  * 对元素的种类有限制。尝试添加一个不合格的元素会抛出一个未检查的异常，特别是
  * NullPointerException或ClassCastException。尝试去查询一个不合格的元素的存在性将会
  * 抛出一个异常或他可能简单的返回false；一些实现会限制前面的行为，有些会限制后面的行为。
  * 大多数情况下，尝试对不合格元素执行操作，如果该元素的完成不会导致将不合格的元素插入到集合
  * 中，则可能引发异常或该操作可能成功，这依赖于实现的选择。这种异常在这个接口的规范中
  * 被标记为可选的。
  *
  * 这个取决于每一个集合来决定他自己的同步策略。在没有得到实现的有力保障的情况下，
  * 异常的行为可能源自于在集合中任何方法的调用，该集合正在被另外的线程修改；
  * 这包含直接调用，传递集合到一个方法中，该方法可能会执行调用，并且使用现存的
  * 迭代器来检测集合。
  *
  * 在集合框架接口中的所有方法被定义在Object类的equal方法的术语中。例如，方法
  * contains方法的规范的意思是：“如果并紧紧如果这个集合至少包含一个元素，返回true，例如
  * o==null ? e==null : o.equals(e)”。这个规范不应该解释为暗示调用集合。包含一个非空参数
  * 将会导致 o.equals(e)被应用到任何元素e中。 实现可以自由实现优化，从而避免equals调用，
  * 例如，首先比较两个元素的哈希值。（hashCode()规范保证不同哈希值的两个对象不可能相等。）
  * 通常情况下，各种集合框架接口的实现可以自由的利用底层object方法指定的行为，只要
  * 实现者认为是合适的。
  *
  * 一些在直接或间接包含集合本身的自引用实例中运行集合的递归遍历操作可能会导致带有异常的失败。
  * 这包含clone()，equals()，hashCode()和toString()方法。实现可以选择性的处理自我参照情景，
  * 但是大多数当前的实现都没有这样去做。
  *
  * 默认的方法实现（继承或其他）并不应用任何同步协议。如果一个集合实现有特定的同步协议，
  * 他一定重写了默认的实现来应用那个协议。
  *
  */
 public interface Collection<E> extends Iterable<E> {
     // Query Operations
 
     /**
      *
      * 返回在这个集合中的元素的个数。如果这个集合中包含了超过Integer.MAX_VALUE的
      * 数量的元素，则这个接口返回Integer.MAX_VALUE。
      */
     int size();
 
     /**
      *
      * 如果这个集合不包含任何元素，则返回true
      */
     boolean isEmpty();
 
     /**
      *
      * 更加正式的，如果或仅仅如果这个集合包含至少一个元素e，例如o == null ? e == null : 
      * o.equals(e)。
      * 
      * @param o 在这个集合中的存在性要被验证的元素
      * @return 如果这个集合包含指定元素，则返回true
      * @throws ClassCastException 如果指定元素类型与集合中的元素不一致，抛出该异常
      * @throws NullPointerException 如果指定的元素为null并且这个集合不允许null元素，抛出该异常
      *
      *
      */
     boolean contains(Object o);
 
     /**
      * 返回在这个集合中的所有元素的迭代器(iterator)。没有关于元素返回顺序的保证(除非，这个集合是一些
      * 类的实例，能够提供这样的保证)。
      *
      * @return 在这个集合中的所有元素的迭代器
      */
     Iterator<E> iterator();
 
     /**
      * 返回包含这个集合中所有元素的一个数组。如果此集合对迭代器返回其元素的顺序作出任何保证，
      * 则此方法必须以相同的顺序返回元素。
      *
      * 返回的数组将会是安全的，因为该集合并不维持对他的引用（换句话说，这个方法必须分配一个新的
      * 数组，即使这个集合是使用数组实现的）。因此调用者可以自由的修改返回的数组。
      *
      * 这个方法在基于数组的和基于集合的API之间扮演一个桥梁的角色。
      *
      * @return 返回包含这个集合的所有元素的数组
      */
     Object[] toArray();
 
     /**
      * 返回包含这个集合所有元素的数组。返回数组的运行时类型是指定数组的运行时类型。如果集合适合
      * 指定的数组，则返回该数组。否则，将使用数组指定的运行时类型和集合的大小分配一个新的数组。
      *
      * 如果这个集合适合指定的数组并有剩余的空间（例如，这个数组比这个集合拥有更多的元素），紧跟
      * 在集合末尾之后的数组中的元素被设置为null。只有当调用方知道该集合中不包含任何null元素的
      * 时候，才能有助于确定这个集合的长度。
      *
      * 如果此集合对迭代器返回其元素的顺序作出任何保证，则此方法必须以相同的顺序返回元素。
      * 
      * 就像上面的toArray()方法一样，这个方法在基于数组的和基于集合的API之间扮演一个桥梁的角色。
      * 但是，这个方法允许精确控制输出数组的运行时类型，可能在某些情况下，可能被用于节省分配花销。
      *
      * 假设X是一个知道只包含字符串的集合。下列的代码可以被用于转换集合为一个新的分配的string数组。
      * 
      *     String[] y = x.toArray(new String[0]);
      *
      * 注意： toArray(new Object[0])与上述的toArray()是一样的。
      *
      * @param T 包含集合的数组的运行时类型
      * @param a 这个集合中的元素要被存储的数组，如果他足够大；否则，一个新的相同的运行时类型的数组将被分配。
      * @return 包含集合中所有元素的数组
      * @throws ArrayStoreException 如果数组中指定的运行时类型不是集合中每个元素的超级类型
      * @throws NullPointerException 如果执行的数组为null
      * 
      */
     <T> T[] toArray(T[] a);

 
     /**
      * 确保这个集合包含指定元素（可选操作）。如果这个集合由于调用而更改则返回true。如果这个集合
      * 不允许修改并且已经包含指定元素的话返回false.
      *
      * 支持此操作的集合可能会对添加到此集合中的元素设置限制。特别的，一些集合会拒绝添加null元素，
      * 其他集合会对添加的元素的类型设置限制。集合类应该在文档中明确指明添加元素的限制。
      *
      * 如果一个集合拒绝添加特定元素，其原因不是因为已经包含该元素的时候，一定要抛出异常，而不是返回false。
      * 这保留了一个不变量，即在调用返回后，集合始终包含指定的元素。
      *
      * 
      * @param e 将确保在集合中存在的元素
      * @return 如果该集合因调用而改变，则返回true
      * @throws UnsupportedOperationException 如果add操作不被这个集合支持
      * @throws ClassCastException 如果指定元素的类不允许被添加到这个集合中
      * @throws NullPointerException 如果指定的元素为null并且这个集合不允许空元素
      * @throws IllegalArgumentException 如果元素的一些属性不允许被添加到这个集合中
      * @throws IllegalStateException 如果由于插入限制，该元素这个时候不能被添加
      */
     boolean add(E e);
 
     /**
      * 如果元素存在，从集合中移除指定元素的单一实力。更正式一些，
      *
      *
      * Removes a single instance of the specified element from this
      * collection, if it is present (optional operation).  More formally,
      * removes an element <tt>e</tt> such that
      * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
      * this collection contains one or more such elements.  Returns
      * <tt>true</tt> if this collection contained the specified element (or
      * equivalently, if this collection changed as a result of the call).
      *
      * @param o element to be removed from this collection, if present
      * @return <tt>true</tt> if an element was removed as a result of this call
      * @throws ClassCastException if the type of the specified element
      *         is incompatible with this collection
      *         (<a href="#optional-restrictions">optional</a>)
      * @throws NullPointerException if the specified element is null and this
      *         collection does not permit null elements
      *         (<a href="#optional-restrictions">optional</a>)
      * @throws UnsupportedOperationException if the <tt>remove</tt> operation
      *         is not supported by this collection
      */
     boolean remove(Object o);
 
 
     // Bulk Operations
 
     /**
      * Returns <tt>true</tt> if this collection contains all of the elements
      * in the specified collection.
      *
      * @param  c collection to be checked for containment in this collection
      * @return <tt>true</tt> if this collection contains all of the elements
      *         in the specified collection
      * @throws ClassCastException if the types of one or more elements
      *         in the specified collection are incompatible with this
      *         collection
      *         (<a href="#optional-restrictions">optional</a>)
      * @throws NullPointerException if the specified collection contains one
      *         or more null elements and this collection does not permit null
      *         elements
      *         (<a href="#optional-restrictions">optional</a>),
      *         or if the specified collection is null.
      * @see    #contains(Object)
      */
     boolean containsAll(Collection<?> c);
 
     /**
      * Adds all of the elements in the specified collection to this collection
      * (optional operation).  The behavior of this operation is undefined if
      * the specified collection is modified while the operation is in progress.
      * (This implies that the behavior of this call is undefined if the
      * specified collection is this collection, and this collection is
      * nonempty.)
      *
      * @param c collection containing elements to be added to this collection
      * @return <tt>true</tt> if this collection changed as a result of the call
      * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
      *         is not supported by this collection
      * @throws ClassCastException if the class of an element of the specified
      *         collection prevents it from being added to this collection
      * @throws NullPointerException if the specified collection contains a
      *         null element and this collection does not permit null elements,
      *         or if the specified collection is null
      * @throws IllegalArgumentException if some property of an element of the
      *         specified collection prevents it from being added to this
      *         collection
      * @throws IllegalStateException if not all the elements can be added at
      *         this time due to insertion restrictions
      * @see #add(Object)
      */
     boolean addAll(Collection<? extends E> c);
 
     /**
      * Removes all of this collection's elements that are also contained in the
      * specified collection (optional operation).  After this call returns,
      * this collection will contain no elements in common with the specified
      * collection.
      *
      * @param c collection containing elements to be removed from this collection
      * @return <tt>true</tt> if this collection changed as a result of the
      *         call
      * @throws UnsupportedOperationException if the <tt>removeAll</tt> method
      *         is not supported by this collection
      * @throws ClassCastException if the types of one or more elements
      *         in this collection are incompatible with the specified
      *         collection
      *         (<a href="#optional-restrictions">optional</a>)
      * @throws NullPointerException if this collection contains one or more
      *         null elements and the specified collection does not support
      *         null elements
      *         (<a href="#optional-restrictions">optional</a>),
      *         or if the specified collection is null
      * @see #remove(Object)
      * @see #contains(Object)
      */
     boolean removeAll(Collection<?> c);
 
     /**
      * Removes all of the elements of this collection that satisfy the given
      * predicate.  Errors or runtime exceptions thrown during iteration or by
      * the predicate are relayed to the caller.
      *
      * @implSpec
      * The default implementation traverses all elements of the collection using
      * its {@link #iterator}.  Each matching element is removed using
      * {@link Iterator#remove()}.  If the collection's iterator does not
      * support removal then an {@code UnsupportedOperationException} will be
      * thrown on the first matching element.
      *
      * @param filter a predicate which returns {@code true} for elements to be
      *        removed
      * @return {@code true} if any elements were removed
      * @throws NullPointerException if the specified filter is null
      * @throws UnsupportedOperationException if elements cannot be removed
      *         from this collection.  Implementations may throw this exception if a
      *         matching element cannot be removed or if, in general, removal is not
      *         supported.
      * @since 1.8
      */
     default boolean removeIf(Predicate<? super E> filter) {
         Objects.requireNonNull(filter);
         boolean removed = false;
         final Iterator<E> each = iterator();
         while (each.hasNext()) {
             if (filter.test(each.next())) {
                 each.remove();
                 removed = true;
             }
         }
         return removed;
     }
 
     /**
      * Retains only the elements in this collection that are contained in the
      * specified collection (optional operation).  In other words, removes from
      * this collection all of its elements that are not contained in the
      * specified collection.
      *
      * @param c collection containing elements to be retained in this collection
      * @return <tt>true</tt> if this collection changed as a result of the call
      * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
      *         is not supported by this collection
      * @throws ClassCastException if the types of one or more elements
      *         in this collection are incompatible with the specified
      *         collection
      *         (<a href="#optional-restrictions">optional</a>)
      * @throws NullPointerException if this collection contains one or more
      *         null elements and the specified collection does not permit null
      *         elements
      *         (<a href="#optional-restrictions">optional</a>),
      *         or if the specified collection is null
      * @see #remove(Object)
      * @see #contains(Object)
      */
     boolean retainAll(Collection<?> c);
 
     /**
      * Removes all of the elements from this collection (optional operation).
      * The collection will be empty after this method returns.
      *
      * @throws UnsupportedOperationException if the <tt>clear</tt> operation
      *         is not supported by this collection
      */
     void clear();
 
 
     // Comparison and hashing
 
     /**
      * Compares the specified object with this collection for equality. <p>
      *
      * While the <tt>Collection</tt> interface adds no stipulations to the
      * general contract for the <tt>Object.equals</tt>, programmers who
      * implement the <tt>Collection</tt> interface "directly" (in other words,
      * create a class that is a <tt>Collection</tt> but is not a <tt>Set</tt>
      * or a <tt>List</tt>) must exercise care if they choose to override the
      * <tt>Object.equals</tt>.  It is not necessary to do so, and the simplest
      * course of action is to rely on <tt>Object</tt>'s implementation, but
      * the implementor may wish to implement a "value comparison" in place of
      * the default "reference comparison."  (The <tt>List</tt> and
      * <tt>Set</tt> interfaces mandate such value comparisons.)<p>
      *
      * The general contract for the <tt>Object.equals</tt> method states that
      * equals must be symmetric (in other words, <tt>a.equals(b)</tt> if and
      * only if <tt>b.equals(a)</tt>).  The contracts for <tt>List.equals</tt>
      * and <tt>Set.equals</tt> state that lists are only equal to other lists,
      * and sets to other sets.  Thus, a custom <tt>equals</tt> method for a
      * collection class that implements neither the <tt>List</tt> nor
      * <tt>Set</tt> interface must return <tt>false</tt> when this collection
      * is compared to any list or set.  (By the same logic, it is not possible
      * to write a class that correctly implements both the <tt>Set</tt> and
      * <tt>List</tt> interfaces.)
      *
      * @param o object to be compared for equality with this collection
      * @return <tt>true</tt> if the specified object is equal to this
      * collection
      *
      * @see Object#equals(Object)
      * @see Set#equals(Object)
      * @see List#equals(Object)
      */
     boolean equals(Object o);
 
     /**
      * Returns the hash code value for this collection.  While the
      * <tt>Collection</tt> interface adds no stipulations to the general
      * contract for the <tt>Object.hashCode</tt> method, programmers should
      * take note that any class that overrides the <tt>Object.equals</tt>
      * method must also override the <tt>Object.hashCode</tt> method in order
      * to satisfy the general contract for the <tt>Object.hashCode</tt> method.
      * In particular, <tt>c1.equals(c2)</tt> implies that
      * <tt>c1.hashCode()==c2.hashCode()</tt>.
      *
      * @return the hash code value for this collection
      *
      * @see Object#hashCode()
      * @see Object#equals(Object)
      */
     int hashCode();
 
     /**
      * Creates a {@link Spliterator} over the elements in this collection.
      *
      * Implementations should document characteristic values reported by the
      * spliterator.  Such characteristic values are not required to be reported
      * if the spliterator reports {@link Spliterator#SIZED} and this collection
      * contains no elements.
      *
      * <p>The default implementation should be overridden by subclasses that
      * can return a more efficient spliterator.  In order to
      * preserve expected laziness behavior for the {@link #stream()} and
      * {@link #parallelStream()}} methods, spliterators should either have the
      * characteristic of {@code IMMUTABLE} or {@code CONCURRENT}, or be
      * <em><a href="Spliterator.html#binding">late-binding</a></em>.
      * If none of these is practical, the overriding class should describe the
      * spliterator's documented policy of binding and structural interference,
      * and should override the {@link #stream()} and {@link #parallelStream()}
      * methods to create streams using a {@code Supplier} of the spliterator,
      * as in:
      * <pre>{@code
      *     Stream<E> s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)
      * }</pre>
      * <p>These requirements ensure that streams produced by the
      * {@link #stream()} and {@link #parallelStream()} methods will reflect the
      * contents of the collection as of initiation of the terminal stream
      * operation.
      *
      * @implSpec
      * The default implementation creates a
      * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
      * from the collections's {@code Iterator}.  The spliterator inherits the
      * <em>fail-fast</em> properties of the collection's iterator.
      * <p>
      * The created {@code Spliterator} reports {@link Spliterator#SIZED}.
      *
      * @implNote
      * The created {@code Spliterator} additionally reports
      * {@link Spliterator#SUBSIZED}.
      *
      * <p>If a spliterator covers no elements then the reporting of additional
      * characteristic values, beyond that of {@code SIZED} and {@code SUBSIZED},
      * does not aid clients to control, specialize or simplify computation.
      * However, this does enable shared use of an immutable and empty
      * spliterator instance (see {@link Spliterators#emptySpliterator()}) for
      * empty collections, and enables clients to determine if such a spliterator
      * covers no elements.
      *
      * @return a {@code Spliterator} over the elements in this collection
      * @since 1.8
      */
     @Override
     default Spliterator<E> spliterator() {
         return Spliterators.spliterator(this, 0);
     }
 
     /**
      * Returns a sequential {@code Stream} with this collection as its source.
      *
      * <p>This method should be overridden when the {@link #spliterator()}
      * method cannot return a spliterator that is {@code IMMUTABLE},
      * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
      * for details.)
      *
      * @implSpec
      * The default implementation creates a sequential {@code Stream} from the
      * collection's {@code Spliterator}.
      *
      * @return a sequential {@code Stream} over the elements in this collection
      * @since 1.8
      */
     default Stream<E> stream() {
         return StreamSupport.stream(spliterator(), false);
     }
 
     /**
      * Returns a possibly parallel {@code Stream} with this collection as its
      * source.  It is allowable for this method to return a sequential stream.
      *
      * <p>This method should be overridden when the {@link #spliterator()}
      * method cannot return a spliterator that is {@code IMMUTABLE},
      * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
      * for details.)
      *
      * @implSpec
      * The default implementation creates a parallel {@code Stream} from the
      * collection's {@code Spliterator}.
      *
      * @return a possibly parallel {@code Stream} over the elements in this
      * collection
      * @since 1.8
      */
     default Stream<E> parallelStream() {
         return StreamSupport.stream(spliterator(), true);
     }
 }


```