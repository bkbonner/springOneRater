package springonerater



import org.junit.*
import grails.test.mixin.*

@TestFor(SpeakerController)
@Mock(Speaker)
class SpeakerControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/speaker/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.speakerInstanceList.size() == 0
        assert model.speakerInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.speakerInstance != null
    }

    void testSave() {
        controller.save()

        assert model.speakerInstance != null
        assert view == '/speaker/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/speaker/show/1'
        assert controller.flash.message != null
        assert Speaker.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/speaker/list'


        populateValidParams(params)
        def speaker = new Speaker(params)

        assert speaker.save() != null

        params.id = speaker.id

        def model = controller.show()

        assert model.speakerInstance == speaker
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/speaker/list'


        populateValidParams(params)
        def speaker = new Speaker(params)

        assert speaker.save() != null

        params.id = speaker.id

        def model = controller.edit()

        assert model.speakerInstance == speaker
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/speaker/list'

        response.reset()


        populateValidParams(params)
        def speaker = new Speaker(params)

        assert speaker.save() != null

        // test invalid parameters in update
        params.id = speaker.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/speaker/edit"
        assert model.speakerInstance != null

        speaker.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/speaker/show/$speaker.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        speaker.clearErrors()

        populateValidParams(params)
        params.id = speaker.id
        params.version = -1
        controller.update()

        assert view == "/speaker/edit"
        assert model.speakerInstance != null
        assert model.speakerInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/speaker/list'

        response.reset()

        populateValidParams(params)
        def speaker = new Speaker(params)

        assert speaker.save() != null
        assert Speaker.count() == 1

        params.id = speaker.id

        controller.delete()

        assert Speaker.count() == 0
        assert Speaker.get(speaker.id) == null
        assert response.redirectedUrl == '/speaker/list'
    }
}
