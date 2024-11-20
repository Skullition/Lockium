package dev.skullition.lockium.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import dev.skullition.lockium.model.GrowtopiaItem;
import java.io.IOException;
import java.util.Set;

/**
 * Deserializes an int property flag for items to a {@link Set} of {@link
 * dev.skullition.lockium.model.GrowtopiaItem.ItemProperty}.
 */
public class ItemPropertyDeserializer extends JsonDeserializer<Set<GrowtopiaItem.ItemProperty>> {
  /**
   * Method that can be called to ask implementation to deserialize JSON content into the value type
   * this serializer handles. Returned instance is to be constructed by method itself.
   *
   * <p>Pre-condition for this method is that the parser points to the first event that is part of
   * value to deserializer (and which is never JSON 'null' literal, more on this below): for simple
   * types it may be the only value; and for structured types the Object start marker or a
   * FIELD_NAME.
   *
   * <p>The two possible input conditions for structured types result from polymorphism via fields.
   * In the ordinary case, Jackson calls this method when it has encountered an OBJECT_START, and
   * the method implementation must advance to the next token to see the first field name. If the
   * application configures polymorphism via a field, then the object looks like the following.
   *
   * <pre>
   *      {
   *          "@class": "class name",
   *          ...
   *      }
   *  </pre>
   * <p></p>
   * Jackson consumes the two tokens (the <tt>@class</tt> field name and its value) in order to
   * learn the class and select the deserializer. Thus, the stream is pointing to the FIELD_NAME for
   * the first field after the @class. Thus, if you want your method to work correctly both with and
   * without polymorphism, you must begin your method with:
   *
   * <pre>
   *       if (p.currentToken() == JsonToken.START_OBJECT) {
   *         p.nextToken();
   *       }
   *  </pre>
   * <p></p>
   * This results in the stream pointing to the field name, so that the two conditions align.
   *
   * <p>Post-condition is that the parser will point to the last event that is part of deserialized
   * value (or in case deserialization fails, event that was not recognized or usable, which may be
   * the same event as the one it pointed to upon call).
   *
   * <p><strong>Handling null values (JsonToken.VALUE_NULL)</strong> <br>
   * Note that this method is never called for the JSON {@code null} literal to avoid every
   * deserializer from having to handle null values. Instead, the {@link
   * JsonDeserializer#getNullValue(DeserializationContext)} method is called to produce a null
   * value. To influence null handling, custom deserializers should override {@link
   * JsonDeserializer#getNullValue(DeserializationContext)} and usually also {@link
   * JsonDeserializer#getNullAccessPattern()}.
   *
   * @param parser Parser used for reading JSON content
   * @param context Context that can be used to access information about this deserialization
   *     activity.
   * @return Deserialized value
   */
  @Override
  public Set<GrowtopiaItem.ItemProperty> deserialize(
      JsonParser parser, DeserializationContext context) throws IOException {
    int flags = parser.getValueAsInt();
    return GrowtopiaItem.ItemProperty.fromInt(flags);
  }
}
