Android-EmptyView
=================

Android custom view with four states (content, loading, empty and error/retry)

## Include view in your layout ##
This example shows the empty view with a ListView, but can be used with all kind of views!

```XML
<com.welbits.izanrodrigo.emptyview.library.EmptyView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@android:id/empty"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@android:id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</com.welbits.izanrodrigo.emptyview.library.EmptyView>
```
## Configuration ##
#### Attrs ####
<table>
  <tr>
    <th>Name</th><th>Type</th><th>Description</th>
  </tr>
  <tr>
    <td>emptyText</td><td>String</td><td>Text shown when displayEmpty() method is called</td>
  </tr>
  <tr>
    <td>errorText</td><td>String</td><td>Text shown when errorLoading() method is called</td>
  </tr>
  <tr>
    <td>fadeAnimation</td><td>Boolean</td><td>Set whether or not show the animation when switching between states</td>
  </tr>
  <tr>
    <td>textSizeSP</td><td>Integer</td><td>Empty and error texts size</td>
  </tr>
  <tr>
    <td>textPadding</td><td>Dimension</td><td>Left and right padding for empty and error texts</td>
  </tr>
</table>

#### XML Example ####
###### In the layout: ######
```XML
<com.welbits.izanrodrigo.emptyview.library.EmptyView
    xmlns:android="http://schemas.android.com/apk/res/android"
    style="@style/EmptyView"
    android:id="@android:id/empty"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    ...
</com.welbits.izanrodrigo.emptyview.library.EmptyView>
```
###### In styles.xml: ######
```XML
<style name="EmptyView">
    <item name="emptyText">@string/empty</item>
    <item name="errorText">@string/error</item>
    <item name="fadeAnimation">true</item>
    <item name="textSizeSP">@integer/textSize</item>
    <item name="textPadding">@dimen/textPadding</item>
</style>
```

To add an error view with a retry button:
```Java
EmptyView emptyView = (EmptyView) findViewById(android.R.id.empty);
emptyView.retry(R.string.retry, this::loadData);
// Second argument can be a Runnable or a View.OnClickListener
```

#### Java Example ####
```Java
emptyView
  .empty(R.string.empty)
  .error(R.string.error)
  .fadeAnimation(true)
  .retry(R.string.retry, this::loadData)
  .textConfigResources(R.dimen.textPadding, R.integer.textSize);
```

## Switch between states ##
The API provides four methods:
<table>
  <tr>
    <th>Method</th><th>Description</th><th>Notes</th>
  </tr>
  <tr>
    <td>startLoading()</td><td>Displays progress bar</td><td></td>
  </tr>
  <tr>
    <td>successLoading()</td><td>Show content (in the above example, the list view)</td><td></td>
  </tr>
  <tr>
    <td>errorLoading()</td><td>Show error message and optionally a retry button</td><td>If you want to add a retry button, use retry method. For further info, see Java Example above.
  </tr>
  <tr>
    <td>displayEmpty()</td><td>Display empty text</td><td></td>
  </tr>
</table>

This is an example (this::loadData):
```Java
private void loadData() {
    emptyView.startLoading();
    listView.postDelayed(() -> {
        // Error
        if (RANDOM.nextBoolean()) {
            if (RANDOM.nextBoolean()) {
               emptyView.errorLoading();
            } else {
               emptyView.displayEmpty();
            }
        }

        // Success
        else {
            adapter.addAll($(0, 25).map(i -> "Item " + (i + 1)).toList());
            emptyView.successLoading();
        }
    }, 2000);
}
```

## Coming soon... ##
- Upload library to maven central
